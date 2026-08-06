package prototype.dom.generator

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

/**
 * Maps a browser type onto its portable counterpart, or reports that it has none.
 *
 * A member is ported only when every type
 * in its signature maps, which keeps the generated API closed over the resolved closure. Members
 * that reach into the rest of the DOM (`DOMTokenList`, `Promise`, `JsArray`, event handlers)
 * are excluded.
 */
internal class PortableTypeMapper(private val closure: Map<String, ClassName>) {
    fun map(type: KSType?): TypeName? {
        if (type == null || type.isError) return null
        // Generic types would need type-parameter support on both actuals; none of the facade
        // members needs it once `ItemArrayLike<T>` has been substituted away by `asMemberOf`.
        if (type.arguments.isNotEmpty()) return null
        val declaration = type.declaration as? KSClassDeclaration ?: return null
        val qualifiedName = declaration.qualifiedName?.asString() ?: return null
        val portable = BUILTIN_TYPES[qualifiedName] ?: closure[qualifiedName] ?: return null
        return portable.copy(nullable = type.isMarkedNullable)
    }
}

internal class ScannedMembers(
    val properties: List<PortableProperty>,
    val functions: List<PortableFunction>,
    /** Signature keys of everything this class contributes, for the subclasses to skip. */
    val keys: Set<String>,
) {
    companion object {
        val EMPTY = ScannedMembers(emptyList(), emptyList(), emptySet())
    }
}

/**
 * Collects the members of one facade class: the ones it declares itself, plus the ones it inherits
 * from supertypes the facade drops. The dropped supertypes are the browser's mixin interfaces
 * (`ParentNode`, `ChildNode`, `ItemArrayLike`, ...); flattening them is what keeps members such as
 * `NodeList.length` and `Element.querySelector` on the facade even though the interfaces are gone.
 */
internal class MemberScanner(
    private val types: PortableTypeMapper,
    private val closureNames: Set<String>,
    private val index: DeclarationIndex,
) {
    fun scan(declaration: KSClassDeclaration, inheritedKeys: Set<String>): ScannedMembers {
        val ownType = declaration.asStarProjectedType()
        val properties = mutableListOf<PortableProperty>()
        val functions = mutableListOf<PortableFunction>()
        val keys = mutableSetOf<String>()

        fun add(member: KSDeclaration) {
            when (member) {
                is KSPropertyDeclaration -> {
                    val property = property(member, ownType) ?: return
                    val key = "val ${property.name}"
                    if (key in inheritedKeys || !keys.add(key)) return
                    properties += property
                }
                is KSFunctionDeclaration -> {
                    val function = function(member, ownType) ?: return
                    val key = function.key()
                    if (key in inheritedKeys || !keys.add(key)) return
                    functions += function
                }
                else -> Unit
            }
        }

        // Declared first, so a class's own signature wins over the inherited one it overrides.
        declaration.getDeclaredProperties().forEach(::add)
        declaration.getDeclaredFunctions().forEach(::add)
        droppedSupertypes(declaration).forEach { supertype ->
            supertype.getDeclaredProperties().forEach(::add)
            supertype.getDeclaredFunctions().forEach(::add)
        }

        return ScannedMembers(properties, functions, keys)
    }

    private fun property(declaration: KSPropertyDeclaration, ownType: KSType): PortableProperty? {
        if (declaration.typeParameters.isNotEmpty() || declaration.extensionReceiver != null) return null
        val resolved = runCatching { declaration.asMemberOf(ownType) }.getOrNull()
        val type = types.map(resolved) ?: types.map(declaration.type.resolve()) ?: return null
        return PortableProperty(
            name = declaration.simpleName.asString(),
            type = type,
            mutable = declaration.isMutable,
            open = declaration.isOpenMember(),
            abstractInBrowser = declaration.isAbstractInBrowser(),
        )
    }

    private fun function(declaration: KSFunctionDeclaration, ownType: KSType): PortableFunction? {
        if (declaration.isConstructor()) return null
        if (declaration.typeParameters.isNotEmpty() || declaration.extensionReceiver != null) return null
        val resolved = runCatching { declaration.asMemberOf(ownType) }.getOrNull()
        if (resolved != null && resolved.isError) return null
        val returnType = types.map(resolved?.returnType)
            ?: types.map(declaration.returnType?.resolve())
            ?: return null

        val parameters = declaration.parameters.mapIndexed { index, parameter ->
            // A vararg parameter substitutes to its element type here; falling back to the declared
            // type also covers the case where substitution reports the array instead.
            val type = types.map(resolved?.parameterTypes?.getOrNull(index))
                ?: types.map(parameter.type.resolve())
                ?: return null
            PortableParameter(
                name = parameter.name?.asString() ?: return null,
                type = type,
                isVararg = parameter.isVararg,
                hasDefault = parameter.hasDefault,
            )
        }

        return PortableFunction(
            name = declaration.simpleName.asString(),
            parameters = parameters,
            returnType = returnType,
            open = declaration.isOpenMember(),
            abstractInBrowser = declaration.isAbstractInBrowser(),
        )
    }

    /**
     * Every supertype the facade does not port, transitively. Portable supertypes stop the walk:
     * their members are emitted on the facade class that stands in for them.
     */
    private fun droppedSupertypes(declaration: KSClassDeclaration): List<KSClassDeclaration> {
        val dropped = mutableListOf<KSClassDeclaration>()
        val seen = mutableSetOf<String>()

        fun walk(current: KSClassDeclaration) {
            for (reference in current.superTypes) {
                val type = reference.resolve()
                if (type.isError) continue
                val resolvedDeclaration = type.declaration as? KSClassDeclaration ?: continue
                val qualifiedName = resolvedDeclaration.qualifiedName?.asString() ?: continue
                if (qualifiedName in closureNames || qualifiedName in IGNORED_SUPERTYPES) continue
                if (!seen.add(qualifiedName)) continue
                // The `actual external` mixins in jsMain have `dynamic` parameters; only the `expect`
                // side still carries the real types.
                val candidate = index.declarationFor(qualifiedName) ?: resolvedDeclaration
                dropped += candidate
                walk(candidate)
            }
        }

        walk(declaration)
        return dropped
    }
}

/**
 * Signature key used to drop a member a supertype already provides. Overrides of a dropped
 * interface become plain members of the class that first inherits them, so a subclass repeating the
 * same signature has nothing left to add.
 */
internal fun PortableFunction.key(): String =
    parameters.joinToString(prefix = "fun $name(", postfix = ")") { parameter ->
        (if (parameter.isVararg) "vararg " else "") + parameter.type
    }

/**
 * A member is `open` on the facade when it is open, abstract or an override in the browser
 * declaration, or when it comes from one of the dropped mixin interfaces. Nothing is emitted as
 * `abstract`: the JVM stubs have to stay instantiable by subclassing alone.
 */
private fun KSDeclaration.isOpenMember(): Boolean =
    Modifier.OPEN in modifiers ||
        Modifier.OVERRIDE in modifiers ||
        Modifier.ABSTRACT in modifiers ||
        (parentDeclaration as? KSClassDeclaration)?.classKind == ClassKind.INTERFACE

/**
 * True for a mixin-interface member the browser leaves abstract. The facade declares it `open`
 * anyway, which the backend sees as a mismatch against the aliased external class — see
 * [PortableClass.needsIrSuppression].
 */
private fun KSDeclaration.isAbstractInBrowser(): Boolean =
    (parentDeclaration as? KSClassDeclaration)?.classKind == ClassKind.INTERFACE && Modifier.OPEN !in modifiers
