package prototype.dom.generator

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier

/**
 * Intermediate model for KSP declarations -> KotlinPoet emitters
 * browserName` is the original `org.w3c.dom` type. Portable
 * names are the safe `kotlinx.browser` types we generate.
 */
internal data class PortableClass(
    val browserName: ClassName,
    val parentBrowserName: ClassName?,
    val shape: ClassShape,
    val sourceFile: KSFile?,
) {
    val portableName: ClassName
        get() = browserName.toPortableName()

    val portableParentName: ClassName?
        get() = parentBrowserName?.toPortableName()
}

// EventTarget lives in the events package, every other declaration maps into the DOM package. (TODO for later)
private fun ClassName.toPortableName(): ClassName =
    if (canonicalName == EVENT_TARGET) PORTABLE_EVENT_TARGET else ClassName(PORTABLE_DOM_PACKAGE, simpleName)

/** The subset of class modifiers the facade preserves; drives inheritance rules per target. */
internal enum class ClassShape {
    ABSTRACT,
    OPEN,
    FINAL,
}

internal fun KSClassDeclaration.shape(): ClassShape = when {
    Modifier.ABSTRACT in modifiers -> ClassShape.ABSTRACT
    Modifier.OPEN in modifiers -> ClassShape.OPEN
    else -> ClassShape.FINAL
}

internal val ClassShape.modifier: KModifier
    get() = when (this) {
        ClassShape.ABSTRACT -> KModifier.ABSTRACT
        ClassShape.OPEN -> KModifier.OPEN
        ClassShape.FINAL -> KModifier.FINAL
    }

/** True for the browser types we port: the DOM package plus the single `EventTarget` supertype. */
internal fun String?.isPortableBrowserClass(): Boolean =
    this == EVENT_TARGET || this?.substringBeforeLast('.') == DOM_PACKAGE

/**
 * The primary class supertype we follow when building the inheritance closure, or `null` if none of
 * the supertypes is a portable browser class. Interfaces are skipped: the facade models the single
 * class hierarchy so `superclass`/`superinterface` can be assigned correctly on each target.
 */
internal fun KSClassDeclaration.findPortableParent(): KSClassDeclaration? =
    superTypes
        .map { it.resolve() }
        .filterNot { it.isError }
        .mapNotNull { it.declaration as? KSClassDeclaration }
        .firstOrNull { candidate ->
            candidate.classKind == ClassKind.CLASS && candidate.qualifiedName?.asString().isPortableBrowserClass()
        }
