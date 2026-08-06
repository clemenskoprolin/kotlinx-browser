package prototype.dom.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec

public class PortableDomProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        PortableDomProcessor(environment.codeGenerator, environment.logger)
}

private class PortableDomProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    private var generated = false

    override fun process(resolver: Resolver): List<KSClassDeclaration> { // might get called multiple times
        if (generated) return emptyList() // guard to make sure files are only generated once

        val index = DeclarationIndex(resolver) // builds browser name -> common declaration index
        val seeds = readAllowlist()
        val closure = linkedMapOf<String, KSClassDeclaration>()
        var failed = false

        fun visit(declaration: KSClassDeclaration) { // recursively finds parents, parent-first ordering
            val qualifiedName = declaration.qualifiedName?.asString()
            if (qualifiedName == null || qualifiedName in closure) return

            val parent = index.portableParent(declaration)
            if (parent != null) visit(parent)

            closure[qualifiedName] = declaration
        }

        for (seed in seeds) { // build inheritance closure by directly look up supported classes
            val qualifiedName = "$DOM_PACKAGE.$seed"
            val declaration = index.declarationFor(qualifiedName)
            if (declaration == null || declaration.containingFile == null) {
                logger.error("Portable DOM seed is not a source declaration: $qualifiedName")
                failed = true
            } else {
                visit(declaration)
            }
        }

        if (failed) return emptyList()

        if (seeds.size != EXPECTED_SEED_COUNT || closure.size != EXPECTED_CLOSURE_COUNT ||
            EVENT_TARGET !in closure
        ) { // Guard to make sure we didn't miss something in first pass
            logger.error(
                "Unexpected portable DOM model: ${seeds.size} seeds, ${closure.size} closure declarations, " +
                    "EventTarget present=${EVENT_TARGET in closure}; " +
                    "expected $EXPECTED_SEED_COUNT/$EXPECTED_CLOSURE_COUNT/true",
            )
            return emptyList()
        }

        val behavioral = nonDictionaryInterfaces(index, closure)
        if (behavioral.isNotEmpty()) { // Guard against the interface == dictionary assumption below
            logger.error(
                "Portable DOM interfaces without an option dictionary factory: " +
                    "${behavioral.joinToString()}. Interfaces are emitted member-free because the " +
                    "browser builds a dictionary from a top-level factory of the same name. A " +
                    "behavioral interface ported that way silently loses the members it declares, and " +
                    "every class that inherits them loses them too — closure membership stops the " +
                    "flattening walk in MemberScanner. Drop it from the allowlist, or teach the " +
                    "generator to scan interface members.",
            )
            return emptyList()
        }

        // KSP declaration -> PortableClass to record members, shapes, etc.
        val model = buildModel(index, closure)
        val eventTarget = model.single { it.browserName.canonicalName == EVENT_TARGET }
        val domDeclarations = model.filter { it.browserName.packageName == DOM_PACKAGE }
        val dictionaries = domDeclarations.filter { it.shape == ClassShape.INTERFACE }
        val classes = domDeclarations.filter { it.shape != ClassShape.INTERFACE }

        val sourceFiles = model.mapNotNull(PortableClass::sourceFile).distinct().toTypedArray()
        val dependencies = Dependencies(aggregating = true, *sourceFiles)

        // The JVM singletons are requested while the classes are rendered, so one instance has to
        // span the whole JVM emission.
        val jvmValues = JvmStubValues(model.associateBy(PortableClass::portableName))

        // emit different source sets based on requirements
        emitSourceSet(
            "commonMain",
            dependencies,
            commonJsAnyFile(),
            commonCoreFile(),
            commonEventsFile(eventTarget),
            commonDictionariesFile(dictionaries),
            commonDomFile(classes),
        )
        emitSourceSet("jsMain", dependencies, jsJsAnyFile())
        emitSourceSet("wasmJsMain", dependencies, browserJsAnyFile())
        emitSourceSet(
            "webMain",
            dependencies,
            webEventsFile(eventTarget),
            webDictionariesFile(dictionaries),
            webDomFile(classes),
        )
        emitSourceSet(
            "jvmMain",
            dependencies,
            jvmJsAnyFile(),
            jvmEventsFile(eventTarget, jvmValues),
            jvmDictionariesFile(dictionaries, jvmValues),
            jvmDomFile(classes, jvmValues),
        )
        emitModelReport(dependencies, seeds, model)

        generated = true
        logger.info(
            "Generated portable DOM facade model with ${model.size} declarations and " +
                "${model.sumOf(PortableClass::memberCount)} members from ${seeds.size} seeds",
        )
        return emptyList()
    }

    /**
     * The closure interfaces that are not option dictionaries, which the facade cannot express.
     *
     * A dictionary is recognised by the top-level factory the browser builds it from, not by its
     * shape: `org.w3c.dom` declares both dictionaries and behavioral mixins (`ElementContentEditable`,
     * `CanvasPath`, `HTMLHyperlinkElementUtils`, ...) as plain interfaces, and only the former has a
     * factory of the same name. Checked against [DeclarationIndex.topLevelFunctionFor] rather than
     * the resulting [PortableFactory], which is also null for a dictionary whose every parameter was
     * dropped by [PortableTypeMapper].
     */
    private fun nonDictionaryInterfaces(
        index: DeclarationIndex,
        closure: Map<String, KSClassDeclaration>,
    ): Set<String> = closure
        .filter { (qualifiedName, declaration) ->
            declaration.shape() == ClassShape.INTERFACE && index.topLevelFunctionFor(qualifiedName) == null
        }
        .keys

    /**
     * Builds portable class model from closure. Parent-first ordering means a class already
     * knows which signatures its ancestors contribute by the time it is scanned.
     */
    private fun buildModel(
        index: DeclarationIndex,
        closure: Map<String, KSClassDeclaration>,
    ): List<PortableClass> {
        val portableNames = closure.keys.associateWith { ClassName.bestGuess(it).toPortableName() } // e.g. org.w3c.dom.Element --> kotlinx.browser.dom.Element

        val mapper = PortableTypeMapper(portableNames) // map type of property / function to supported once (if able)
        val scanner = MemberScanner(mapper, closure.keys, index)
        val inheritedKeys = mutableMapOf<String, Set<String>>()
        val ancestors = mutableMapOf<String, List<ClassName>>()

        return closure.map { (qualifiedName, declaration) ->
            val parentName = index.portableParent(declaration)?.qualifiedName?.asString()
            val inherited = parentName?.let(inheritedKeys::getValue).orEmpty()
            val shape = declaration.shape()
            // Option dictionaries stay opaque: the browser builds them from a factory, so only that
            // factory is ported and the interface itself carries no members.
            val members = if (shape == ClassShape.INTERFACE) {
                ScannedMembers.EMPTY
            } else {
                scanner.scan(declaration, inherited)
            }
            inheritedKeys[qualifiedName] = inherited + members.keys // to not regenerate inherited members
            ancestors[qualifiedName] = parentName
                ?.let { listOf(portableNames.getValue(it)) + ancestors.getValue(it) }
                .orEmpty()

            PortableClass(
                browserName = ClassName.bestGuess(qualifiedName),
                parentBrowserName = parentName?.let(ClassName::bestGuess),
                ancestors = ancestors.getValue(qualifiedName),
                shape = shape,
                properties = members.properties,
                functions = members.functions,
                factory = if (shape == ClassShape.INTERFACE) index.factoryFor(qualifiedName, mapper) else null,
                sourceFile = declaration.containingFile,
            )
        }
    }

    /**
     * The top-level function that builds an option dictionary, keeping only the parameters the
     * facade can express. The rest keep the browser factory's own defaults.
     */
    private fun DeclarationIndex.factoryFor(qualifiedName: String, mapper: PortableTypeMapper): PortableFactory? {
        val factory = topLevelFunctionFor(qualifiedName) ?: return null
        val parameters = factory.parameters.mapNotNull { parameter ->
            val type = mapper.map(parameter.type.resolve()) ?: return@mapNotNull null
            PortableParameter(
                name = parameter.name?.asString() ?: return@mapNotNull null,
                type = type,
                isVararg = parameter.isVararg,
                hasDefault = true,
            )
        }
        return if (parameters.isEmpty()) null else PortableFactory(parameters)
    }

    // KSP writes into a single target compilation, not a KMP source set, so each source set's files
    // are staged as resources under STAGING_ROOT/<sourceSet>/... for the Gradle build to unpack.
    private fun emitSourceSet(sourceSet: String, dependencies: Dependencies, vararg files: FileSpec) {
        for (file in files) {
            val stagingPackage = listOf(STAGING_ROOT, sourceSet, "kotlin", file.packageName)
                .filter(String::isNotEmpty)
                .joinToString(".")
            codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = stagingPackage,
                fileName = file.name,
                extensionName = "kt.txt",
            ).bufferedWriter().use(file::writeTo)
        }
    }

    private fun emitModelReport(
        dependencies: Dependencies,
        seeds: List<String>,
        model: List<PortableClass>,
    ) {
        codeGenerator.createNewFile(dependencies, STAGING_ROOT, "model", "txt").bufferedWriter().use { writer ->
            writer.appendLine("seeds=${seeds.size}")
            writer.appendLine("closure=${model.size}")
            writer.appendLine("members=${model.sumOf(PortableClass::memberCount)}")
            model.forEach { declaration ->
                writer.append(declaration.browserName.canonicalName)
                writer.append('|')
                writer.append(declaration.parentBrowserName?.canonicalName.orEmpty())
                writer.append('|')
                writer.append(declaration.shape.name.lowercase())
                writer.append('|')
                writer.appendLine(declaration.memberCount.toString())
                declaration.properties.forEach { property ->
                    writer.append("  ")
                    writer.append(if (property.mutable) "var " else "val ")
                    writer.append(property.name)
                    writer.append(": ")
                    writer.appendLine(property.type.toString())
                }
                declaration.functions.forEach { function ->
                    writer.append("  ")
                    writer.append(function.key())
                    writer.append(": ")
                    writer.appendLine(function.returnType.toString())
                }
            }
        }
    }

    private fun readAllowlist(): List<String> =
        checkNotNull(javaClass.getResourceAsStream("/compose-html-dom-allowlist.txt")) {
            "Missing Compose HTML DOM allowlist"
        }.bufferedReader().useLines { lines ->
            lines.map(String::trim)
                .filter { it.isNotEmpty() && !it.startsWith('#') }
                .distinct()
                .sorted()
                .toList()
        }
}
