package prototype.dom.generator

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

/**
 * Supplies the values the JVM actuals need: initializers for every stub property, real defaults
 * behind the common `definedExternally` placeholders, and return values for the stub bodies.
 *
 * Most types have an obvious inert value. A non-null facade type does not, so the first time one is
 * asked for the generator records it and emits a private singleton for it alongside the classes.
 */
internal class JvmStubValues(private val classes: Map<ClassName, PortableClass>) {
    private val singletons = linkedMapOf<ClassName, ClassName>()

    fun value(type: TypeName): CodeBlock = inertLiteral(type, classes) ?: singleton(type)

    /** The singletons requested so far, as `object` declarations to append to the same file. */
    fun singletonTypes(): List<TypeSpec> = singletons.map { (facadeType, singletonName) ->
        val builder = TypeSpec.objectBuilder(singletonName).addModifiers(KModifier.PRIVATE)
        when (classes.getValue(facadeType).shape) {
            ClassShape.INTERFACE -> builder.addSuperinterface(facadeType)
            else -> builder.superclass(facadeType)
        }
        builder.build()
    }

    private fun singleton(type: TypeName): CodeBlock {
        val facadeType = type as? ClassName ?: error("No JVM stub value for $type")
        require(facadeType in classes) { "No JVM stub value for $facadeType" }
        val name = singletons.getOrPut(facadeType) {
            ClassName(facadeType.packageName, "Empty${facadeType.simpleName}")
        }
        return CodeBlock.of("%T", name)
    }
}

/**
 * An inert value for [type] that stands on its own, or `null` when only a singleton will do. Also
 * used for the common factory defaults, which have to be real values rather than the
 * `definedExternally` placeholder because the factories are actualized without a typealias.
 */
internal fun inertLiteral(type: TypeName, classes: Map<ClassName, PortableClass> = emptyMap()): CodeBlock? {
    if (type.isNullable) return CodeBlock.of("null")
    return when (type) {
        BOOLEAN -> CodeBlock.of("false")
        STRING -> CodeBlock.of("%S", "")
        BYTE, SHORT, INT -> CodeBlock.of("0")
        LONG -> CodeBlock.of("0L")
        FLOAT -> CodeBlock.of("0.0F")
        DOUBLE -> CodeBlock.of("0.0")
        CHAR -> CodeBlock.of("' '")
        else -> {
            val facade = classes[type] ?: return null
            when (facade.shape) {
                // Option dictionaries are built through the generated factory ...
                ClassShape.INTERFACE -> facade.factory?.let { CodeBlock.of("%T()", type) }
                // ... and any concrete facade class through its own constructor.
                ClassShape.OPEN, ClassShape.FINAL -> CodeBlock.of("%T()", type)
                ClassShape.ABSTRACT -> null
            }
        }
    }
}
