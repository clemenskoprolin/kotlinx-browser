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

        val seeds = readAllowlist()
        val declarations = linkedMapOf<String, PortableClass>()
        var failed = false

        fun visit(declaration: KSClassDeclaration) {  // recursively finds parents, parent-first ordering
            val qualifiedName = declaration.qualifiedName?.asString()
            if (qualifiedName == null || qualifiedName in declarations) return

            val parent = declaration.findPortableParent()
            if (parent != null) visit(parent)

            declarations[qualifiedName] = PortableClass(
                browserName = ClassName.bestGuess(qualifiedName),
                parentBrowserName = parent?.qualifiedName?.asString()?.let(ClassName::bestGuess),
                shape = declaration.shape(),
                sourceFile = declaration.containingFile,
            )
        }

        for (seed in seeds) { // directly look up supported classes
            val qualifiedName = "$DOM_PACKAGE.$seed"
            val declaration = resolver.getClassDeclarationByName(resolver.getKSNameFromString(qualifiedName))
            if (declaration == null || declaration.containingFile == null) {
                logger.error("Portable DOM seed is not a source declaration: $qualifiedName")
                failed = true
            } else {
                visit(declaration)
            }
        }

        if (failed) return emptyList()

        val model = declarations.values.toList()
        val eventTarget = model.singleOrNull { it.browserName.canonicalName == EVENT_TARGET }
        val domClasses = model.filter { it.browserName.packageName == DOM_PACKAGE }

        if (seeds.size != EXPECTED_SEED_COUNT || model.size != EXPECTED_CLOSURE_COUNT || eventTarget == null) { //Guard to make sure we didn't miss something
            logger.error(
                "Unexpected portable DOM model: ${seeds.size} seeds, ${model.size} closure declarations, " +
                    "EventTarget present=${eventTarget != null}; expected $EXPECTED_SEED_COUNT/$EXPECTED_CLOSURE_COUNT/true",
            )
            return emptyList()
        }

        val sourceFiles = model.mapNotNull(PortableClass::sourceFile).distinct().toTypedArray()
        val dependencies = Dependencies(aggregating = true, *sourceFiles)

        // generate files with dependencies
        emitSourceSet("commonMain", dependencies, commonJsAnyFile(), commonEventsFile(eventTarget), commonDomFile(domClasses))
        emitSourceSet("jsMain", dependencies, jsJsAnyFile(), browserEventsFile(eventTarget), browserDomFile(domClasses))
        emitSourceSet("wasmJsMain", dependencies, browserJsAnyFile(), browserEventsFile(eventTarget), browserDomFile(domClasses))
        emitSourceSet("jvmMain", dependencies, jvmJsAnyFile(), jvmEventsFile(eventTarget), jvmDomFile(domClasses))
        emitModelReport(dependencies, seeds, model)

        generated = true
        logger.info("Generated portable DOM facade model with ${model.size} declarations from ${seeds.size} seeds")
        return emptyList()
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
            model.forEach { declaration ->
                writer.append(declaration.browserName.canonicalName)
                writer.append('|')
                writer.append(declaration.parentBrowserName?.canonicalName.orEmpty())
                writer.append('|')
                writer.appendLine(declaration.shape.name.lowercase())
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
