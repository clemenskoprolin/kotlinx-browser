package prototype.dom.generator

import com.squareup.kotlinpoet.ANY as KOTLIN_ANY
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeAliasSpec
import com.squareup.kotlinpoet.TypeSpec

/*
 * KotlinPoet emitters. Each source set gets three files with matching package/name across targets so
 * `expect` in commonMain lines up with the per-target `actual`:
 *   - PortableJsAny  : the `JsAny` marker (see the table below)
 *   - PortableEvents : the `EventTarget` root
 *   - PortableDom    : every DOM class in the closure
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

// --- EventTarget -----------------------------------------------------------------------------
// The DOM hierarchy roots at EventTarget, so it gets its own file in the events package.

internal fun commonEventsFile(eventTarget: PortableClass): FileSpec =
    FileSpec.builder(PORTABLE_EVENTS_PACKAGE, "PortableEvents")
        .generatedComment()
        .addType(eventTarget.commonType())
        .build()

internal fun browserEventsFile(eventTarget: PortableClass): FileSpec =
    FileSpec.builder(PORTABLE_EVENTS_PACKAGE, "PortableEvents")
        .generatedComment()
        .addTypeAlias(eventTarget.browserTypeAlias())
        .build()

internal fun jvmEventsFile(eventTarget: PortableClass): FileSpec =
    FileSpec.builder(PORTABLE_EVENTS_PACKAGE, "PortableEvents")
        .generatedComment()
        .addType(eventTarget.jvmType())
        .build()

// --- DOM classes -----------------------------------------------------------------------------

internal fun commonDomFile(classes: List<PortableClass>): FileSpec = FileSpec.builder(PORTABLE_DOM_PACKAGE, "PortableDom")
    .generatedComment()
    .apply { classes.forEach { addType(it.commonType()) } }
    .build()

internal fun browserDomFile(classes: List<PortableClass>): FileSpec = FileSpec.builder(PORTABLE_DOM_PACKAGE, "PortableDom")
    .generatedComment()
    .apply { classes.forEach { addTypeAlias(it.browserTypeAlias()) } }
    .build()

internal fun jvmDomFile(classes: List<PortableClass>): FileSpec = FileSpec.builder(PORTABLE_DOM_PACKAGE, "PortableDom")
    .generatedComment()
    .apply { classes.forEach { addType(it.jvmType()) } }
    .build()

// --- Per-declaration renderers ---------------------------------------------------------------

// commonMain: `expect` classifier that extends its portable parent (if any) and the JsAny marker.
private fun PortableClass.commonType(): TypeSpec = TypeSpec.classBuilder(portableName)
    .addModifiers(KModifier.PUBLIC, KModifier.EXPECT, shape.modifier)
    .apply {
        portableParentName?.let(::addSuperinterface)
        addSuperinterface(PORTABLE_JS_ANY)
    }
    .build()

// Web targets: `actual typealias` back to the original org.w3c.dom type, preserving type identity.
private fun PortableClass.browserTypeAlias(): TypeAliasSpec = TypeAliasSpec.builder(portableName.simpleName, browserName)
    .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL)
    .build()

// JVM: `actual` marker class. The parent is a real superclass here (no browser typealias exists).
private fun PortableClass.jvmType(): TypeSpec = TypeSpec.classBuilder(portableName)
    .addModifiers(KModifier.PUBLIC, KModifier.ACTUAL, shape.modifier)
    .apply {
        portableParentName?.let(::superclass)
        addSuperinterface(PORTABLE_JS_ANY)
    }
    .build()

private fun FileSpec.Builder.generatedComment(): FileSpec.Builder =
    addFileComment("Generated by the portable DOM KSP prototype. Do not edit.")
