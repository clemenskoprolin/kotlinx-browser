package prototype.dom.generator

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName

/**
 * Intermediate model for KSP declarations -> KotlinPoet emitters.
 * `browserName` is the original `org.w3c.dom` type. Portable names are the safe `kotlinx.browser`
 * types we generate.
 */
internal data class PortableClass(
    val browserName: ClassName,
    val parentBrowserName: ClassName?,
    /** Portable names of every class above this one, nearest first. */
    val ancestors: List<ClassName>,
    val shape: ClassShape,
    val properties: List<PortableProperty>,
    val functions: List<PortableFunction>,
    val factory: PortableFactory?,
    val sourceFile: KSFile?,
) {
    val portableName: ClassName
        get() = browserName.toPortableName()

    val portableParentName: ClassName?
        get() = parentBrowserName?.toPortableName()

    val memberCount: Int
        get() = properties.size + functions.size

    val hasDefaultArguments: Boolean
        get() = functions.any { function -> function.parameters.any(PortableParameter::hasDefault) }

    /**
     * Whether the common declaration needs `EXPECT_ACTUAL_IR_INCOMPATIBILITY` suppressed. Two shapes
     * reach the backend as a mismatch even though the frontend accepts them: an `open` member with
     * default arguments, and a member the facade has to declare `open` because nothing is emitted as
     * `abstract`, while the browser declares it abstract on a mixin interface.
     */
    val needsIrSuppression: Boolean
        get() = properties.any(PortableProperty::abstractInBrowser) ||
            functions.any { it.abstractInBrowser || (it.open && it.parameters.any(PortableParameter::hasDefault)) }
}

internal data class PortableProperty(
    val name: String,
    val type: TypeName,
    val mutable: Boolean,
    val open: Boolean,
    val abstractInBrowser: Boolean,
)

internal data class PortableFunction(
    val name: String,
    val parameters: List<PortableParameter>,
    val returnType: TypeName,
    val open: Boolean,
    val abstractInBrowser: Boolean,
)

internal data class PortableParameter(
    val name: String,
    val type: TypeName,
    val isVararg: Boolean,
    val hasDefault: Boolean,
)

/**
 * The top-level factory that builds an option dictionary. Parameters the facade cannot express are
 * dropped, so the web actual passes the remaining ones by name and lets the browser factory default
 * the rest.
 */
internal data class PortableFactory(val parameters: List<PortableParameter>)

// EventTarget lives in the events package, every other declaration maps into the DOM package. (TODO for later)
internal fun ClassName.toPortableName(): ClassName =
    if (canonicalName == EVENT_TARGET) PORTABLE_EVENT_TARGET else ClassName(PORTABLE_DOM_PACKAGE, simpleName)

/** The subset of class modifiers the facade preserves; drives inheritance rules per target. */
internal enum class ClassShape {
    INTERFACE,
    ABSTRACT,
    OPEN,
    FINAL,
}

internal fun KSClassDeclaration.shape(): ClassShape = when {
    classKind == ClassKind.INTERFACE -> ClassShape.INTERFACE
    Modifier.ABSTRACT in modifiers -> ClassShape.ABSTRACT
    Modifier.OPEN in modifiers -> ClassShape.OPEN
    else -> ClassShape.FINAL
}

internal val ClassShape.modifier: KModifier?
    get() = when (this) {
        ClassShape.INTERFACE -> null
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
 * class hierarchy so `superclass`/`superinterface` can be assigned correctly on each target. Their
 * members are not lost — [MemberScanner] flattens them into the class that inherits them.
 */
internal fun KSClassDeclaration.findPortableParent(): KSClassDeclaration? =
    superTypes
        .map { it.resolve() }
        .filterNot { it.isError }
        .mapNotNull { it.declaration as? KSClassDeclaration }
        .firstOrNull { candidate ->
            candidate.classKind == ClassKind.CLASS && candidate.qualifiedName?.asString().isPortableBrowserClass()
        }
