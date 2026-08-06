package prototype.dom.generator

import com.squareup.kotlinpoet.ANY as KOTLIN_ANY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.NOTHING
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeAliasSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT

/*
 * KotlinPoet emitters. Each source set gets the same package/file names so `expect` in commonMain
 * lines up with the per-target `actual`:
 *   - PortableJsAny       : the `JsAny` marker (see the table below)
 *   - Core                : the `definedExternally` placeholder, commonMain only
 *   - PortableEvents      : the `EventTarget` root
 *   - OptionDictionaries  : the option dictionaries reached by defaulted members, plus factories
 *   - PortableDom         : every DOM class in the closure
 *
 * The web actuals are typealiases, so they are emitted once into webMain and shared by JS and
 * Wasm/JS; only `JsAny` has to differ per target.
 */

// --- JsAny -----------------------------------------------------------------------------------
// The common declaration is an `expect interface`; the three actuals deliberately differ:
//   JS      -> typealias to kotlin.Any, because kotlin.js.JsAny is itself a typealias on JS and
//              Kotlin rejects a chained `actual typealias`.
//   Wasm/JS -> typealias to the real kotlin.js.JsAny interop classifier.
//   JVM     -> a plain marker interface; there is no browser runtime to point at.

internal fun commonJsAnyFile(): FileSpec = FileSpec.builder(PORTABLE_JS_PACKAGE, "PortableJsAny")
    .generatedComment()
    .addType(
        TypeSpec.interfaceBuilder(PORTABLE_JS_ANY)
            .addModifiers(KModifier.PUBLIC, KModifier.EXPECT)
            .build(),
    )
    .build()

internal fun jsJsAnyFile(): FileSpec = FileSpec.builder(PORTABLE_JS_PACKAGE, "PortableJsAny")
    .generatedComment()
    .addTypeAlias(
        TypeAliasSpec.builder(PORTABLE_JS_ANY.simpleName, KOTLIN_ANY)
            .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL)
            .build(),
    )
    .build()

internal fun browserJsAnyFile(): FileSpec = FileSpec.builder(PORTABLE_JS_PACKAGE, "PortableJsAny")
    .generatedComment()
    .addAliasedImport(BROWSER_JS_ANY, BROWSER_JS_ANY_ALIAS)
    .addTypeAlias(
        TypeAliasSpec.builder(PORTABLE_JS_ANY.simpleName, BROWSER_JS_ANY)
            .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL)
            .build(),
    )
    .build()

internal fun jvmJsAnyFile(): FileSpec = FileSpec.builder(PORTABLE_JS_PACKAGE, "PortableJsAny")
    .generatedComment()
    .addType(
        TypeSpec.interfaceBuilder(PORTABLE_JS_ANY)
            .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL)
            .build(),
    )
    .build()

// --- definedExternally -----------------------------------------------------------------------
// A common Nothing-typed placeholder that lets an `expect` member repeat the browser signature
// verbatim, defaults included. On web the member is actualized by a typealias to the external
// declaration, which carries the real defaults, so this value is never read. On JVM the `actual`
// member declares its own defaults.

internal fun commonCoreFile(): FileSpec = FileSpec.builder(PORTABLE_DOM_PACKAGE, "Core")
    .generatedComment()
    .addProperty(
        PropertySpec.builder(DEFINED_EXTERNALLY.simpleName, NOTHING)
            .addModifiers(KModifier.PUBLIC)
            .addKdoc(
                "Stands in for `kotlin.js.definedExternally` on an `expect` member. Never evaluated: " +
                    "web actuals are typealiases to the external declaration and JVM actuals declare " +
                    "their own defaults.",
            )
            .getter(
                FunSpec.getterBuilder()
                    .addStatement(
                        "throw %T(%S)",
                        ClassName("kotlin", "UnsupportedOperationException"),
                        "definedExternally is never evaluated",
                    )
                    .build(),
            )
            .build(),
    )
    .build()

// --- EventTarget -----------------------------------------------------------------------------
// The DOM hierarchy roots at EventTarget, so it gets its own file in the events package.

internal fun commonEventsFile(eventTarget: PortableClass): FileSpec =
    FileSpec.builder(PORTABLE_EVENTS_PACKAGE, "PortableEvents")
        .generatedComment()
        .addType(eventTarget.commonType())
        .build()

internal fun webEventsFile(eventTarget: PortableClass): FileSpec =
    FileSpec.builder(PORTABLE_EVENTS_PACKAGE, "PortableEvents")
        .generatedComment()
        .webSuppressions(listOf(eventTarget))
        .addTypeAlias(eventTarget.browserTypeAlias())
        .build()

internal fun jvmEventsFile(eventTarget: PortableClass, values: JvmStubValues): FileSpec =
    FileSpec.builder(PORTABLE_EVENTS_PACKAGE, "PortableEvents")
        .generatedComment()
        .jvmSuppressions(listOf(eventTarget))
        .addType(eventTarget.jvmType(values))
        .build()

// --- Option dictionaries ---------------------------------------------------------------------
// Dictionaries are opaque on the facade: the browser builds them from a factory function, so the
// interfaces stay member-free and only the factory is ported.

internal fun commonDictionariesFile(dictionaries: List<PortableClass>): FileSpec =
    FileSpec.builder(PORTABLE_DOM_PACKAGE, "OptionDictionaries")
        .generatedComment()
        .apply {
            dictionaries.forEach { addType(it.commonType()) }
            /*
             * Top-level factories are actualized independently of any typealias, so they keep their
             * defaults on the common `expect` declaration without needing a suppression.
             */
            dictionaries.forEach { dictionary ->
                dictionary.factory?.let { addFunction(dictionary.commonFactory(it)) }
            }
        }
        .build()

internal fun webDictionariesFile(dictionaries: List<PortableClass>): FileSpec =
    FileSpec.builder(PORTABLE_DOM_PACKAGE, "OptionDictionaries")
        .generatedComment()
        .webSuppressions(dictionaries)
        .apply {
            dictionaries.forEach { addTypeAlias(it.browserTypeAlias()) }
            dictionaries.forEach { dictionary ->
                dictionary.factory?.let { addFunction(dictionary.webFactory(it)) }
            }
        }
        .build()

internal fun jvmDictionariesFile(dictionaries: List<PortableClass>, values: JvmStubValues): FileSpec =
    FileSpec.builder(PORTABLE_DOM_PACKAGE, "OptionDictionaries")
        .generatedComment()
        .jvmSuppressions(dictionaries)
        .apply {
            dictionaries.forEach { addType(it.jvmType(values)) }
            dictionaries.forEach { dictionary ->
                val factory = dictionary.factory ?: return@forEach
                val instance = dictionary.jvmDictionaryInstance()
                addType(
                    TypeSpec.objectBuilder(instance)
                        .addModifiers(KModifier.PRIVATE)
                        .addSuperinterface(dictionary.portableName)
                        .build(),
                )
                addFunction(dictionary.jvmFactory(factory, instance))
            }
        }
        .build()

// --- DOM classes -----------------------------------------------------------------------------

internal fun commonDomFile(classes: List<PortableClass>): FileSpec =
    FileSpec.builder(PORTABLE_DOM_PACKAGE, "PortableDom")
        .generatedComment()
        .apply { classes.forEach { addType(it.commonType()) } }
        .build()

internal fun webDomFile(classes: List<PortableClass>): FileSpec =
    FileSpec.builder(PORTABLE_DOM_PACKAGE, "PortableDom")
        .generatedComment()
        .webSuppressions(classes)
        .apply { classes.forEach { addTypeAlias(it.browserTypeAlias()) } }
        .build()

internal fun jvmDomFile(classes: List<PortableClass>, values: JvmStubValues): FileSpec =
    FileSpec.builder(PORTABLE_DOM_PACKAGE, "PortableDom")
        .generatedComment()
        .jvmSuppressions(classes)
        .apply {
            classes.forEach { addType(it.jvmType(values)) }
            // Requested while the classes above were emitted, so this has to come last.
            values.singletonTypes().forEach(::addType)
        }
        .build()

// --- Per-declaration renderers ---------------------------------------------------------------

// commonMain: `expect` classifier that extends its portable parent (if any) and the JsAny marker.
private fun PortableClass.commonType(): TypeSpec = typeBuilder()
    .addModifiers(KModifier.PUBLIC, KModifier.EXPECT)
    .apply {
        shape.modifier?.let { addModifiers(it) }
        if (needsIrSuppression) addAnnotation(suppression(IR_SUPPRESSION))
        portableParentName?.let(::addSuperinterface)
        addSuperinterface(PORTABLE_JS_ANY)
        properties.forEach { addProperty(it.commonSpec()) }
        functions.forEach { addFunction(it.commonSpec()) }
    }
    .build()

// Web targets: `actual typealias` back to the original org.w3c.dom type, preserving type identity.
private fun PortableClass.browserTypeAlias(): TypeAliasSpec =
    TypeAliasSpec.builder(portableName.simpleName, browserName)
        .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL)
        .build()

// JVM: `actual` stub. The parent is a real superclass here (no browser typealias exists) and every
// member gets an inert body, so the facade stays subclassable without implementing anything.
private fun PortableClass.jvmType(values: JvmStubValues): TypeSpec = typeBuilder()
    .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL)
    .apply {
        shape.modifier?.let { addModifiers(it) }
        when (shape) {
            ClassShape.INTERFACE -> portableParentName?.let(::addSuperinterface)
            else -> portableParentName?.let(::superclass)
        }
        addSuperinterface(PORTABLE_JS_ANY)
        properties.forEach { addProperty(it.jvmSpec(values)) }
        functions.forEach { addFunction(it.jvmSpec(this@jvmType, values)) }
    }
    .build()

private fun PortableClass.typeBuilder(): TypeSpec.Builder = when (shape) {
    ClassShape.INTERFACE -> TypeSpec.interfaceBuilder(portableName)
    else -> TypeSpec.classBuilder(portableName)
}

// --- Members ---------------------------------------------------------------------------------

private fun PortableProperty.commonSpec(): PropertySpec = PropertySpec.builder(name, type)
    .mutable(mutable)
    .apply { if (open) addModifiers(KModifier.OPEN) }
    .build()

private fun PortableProperty.jvmSpec(values: JvmStubValues): PropertySpec = PropertySpec.builder(name, type)
    .mutable(mutable)
    .addModifiers(KModifier.ACTUAL)
    .apply { if (open) addModifiers(KModifier.OPEN) }
    .initializer(values.value(type))
    .build()

private fun PortableFunction.commonSpec(): FunSpec {
    val function = this
    return FunSpec.builder(name)
        .apply {
            if (function.open) addModifiers(KModifier.OPEN)
            if (function.returnType != UNIT) returns(function.returnType)
            function.parameters.forEach {
                addParameter(it.spec(if (it.hasDefault) CodeBlock.of("%M", DEFINED_EXTERNALLY) else null))
            }
        }
        .build()
}

private fun PortableFunction.jvmSpec(owner: PortableClass, values: JvmStubValues): FunSpec {
    val function = this
    return FunSpec.builder(name)
        .addModifiers(KModifier.ACTUAL)
        .apply {
            if (function.open) addModifiers(KModifier.OPEN)
            function.parameters.forEach {
                addParameter(it.spec(if (it.hasDefault) values.value(it.type) else null))
            }
            if (function.returnType != UNIT) {
                returns(function.returnType)
                addStatement("return %L", function.jvmResult(owner, values))
            }
        }
        .build()
}

/**
 * What a stub returns: an argument of the same type when there is one, then the receiver when the
 * class is itself an instance of a non-null return type, and only otherwise a manufactured value.
 * That is enough for the tree-shaped members (`appendChild`, `getRootNode`, `splitText`) to behave
 * sanely, while a nullable result — a lookup that found nothing — stays `null`.
 */
private fun PortableFunction.jvmResult(owner: PortableClass, values: JvmStubValues): CodeBlock {
    val bare = returnType.copy(nullable = false)
    val argument = parameters.firstOrNull { !it.isVararg && it.type == bare }
    if (argument != null) return CodeBlock.of("%N", argument.name)
    if (!returnType.isNullable && owner.isInstanceOf(bare)) return CodeBlock.of("this")
    return values.value(returnType)
}

private fun PortableClass.isInstanceOf(type: TypeName): Boolean =
    portableName == type || ancestors.any { it == type }

private fun PortableParameter.spec(default: CodeBlock?): ParameterSpec =
    ParameterSpec.builder(name, type, *(if (isVararg) arrayOf(KModifier.VARARG) else emptyArray()))
        .apply { default?.let(::defaultValue) }
        .build()

// --- Dictionary factories --------------------------------------------------------------------

private fun PortableClass.commonFactory(factory: PortableFactory): FunSpec =
    FunSpec.builder(portableName.simpleName)
        .addModifiers(KModifier.PUBLIC, KModifier.EXPECT)
        .returns(portableName)
        .apply { factory.parameters.forEach { addParameter(it.spec(inertLiteral(it.type))) } }
        .build()

private fun PortableClass.webFactory(factory: PortableFactory): FunSpec =
    FunSpec.builder(portableName.simpleName)
        .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL)
        .returns(portableName)
        .apply { factory.parameters.forEach { addParameter(it.spec(null)) } }
        // Written out in full: the file already imports the browser classifier of the same name.
        .addStatement(
            "return %L(%L)",
            browserName.canonicalName,
            factory.parameters.joinToString { "${it.name} = ${it.name}" },
        )
        .build()

private fun PortableClass.jvmFactory(factory: PortableFactory, instance: ClassName): FunSpec =
    FunSpec.builder(portableName.simpleName)
        .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL)
        .returns(portableName)
        .apply { factory.parameters.forEach { addParameter(it.spec(null)) } }
        .addStatement("return %T", instance)
        .build()

private fun PortableClass.jvmDictionaryInstance(): ClassName =
    ClassName(portableName.packageName, "Jvm${portableName.simpleName}")

// --- Shared helpers --------------------------------------------------------------------------

private fun FileSpec.Builder.generatedComment(): FileSpec.Builder =
    addFileComment("Generated by the portable DOM KSP prototype. Do not edit.")

/*
 * Every suppression also raises ERROR_SUPPRESSION, so a file only carries the ones its own contents
 * actually need: a typealias actual is checked member by member against the expect, and only a
 * member with defaults trips the default-argument rules.
 */

private fun FileSpec.Builder.webSuppressions(classes: List<PortableClass>): FileSpec.Builder =
    if (classes.none { it.memberCount > 0 }) this else suppress(*WEB_SUPPRESSIONS)

private fun FileSpec.Builder.jvmSuppressions(classes: List<PortableClass>): FileSpec.Builder =
    if (classes.none { it.hasDefaultArguments }) this else suppress(*JVM_SUPPRESSIONS)

private fun FileSpec.Builder.suppress(vararg names: String): FileSpec.Builder = addAnnotation(
    suppression(*names).toBuilder().useSiteTarget(AnnotationSpec.UseSiteTarget.FILE).build(),
)

private fun suppression(vararg names: String): AnnotationSpec =
    AnnotationSpec.builder(Suppress::class)
        .apply { names.forEach { addMember("%S", it) } }
        .build()
