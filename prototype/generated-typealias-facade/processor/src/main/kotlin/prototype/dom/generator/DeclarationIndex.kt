package prototype.dom.generator

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * Resolves a browser name onto the declaration the generator reads it from.
 *
 * KSP runs on the JS compilation, where a browser name resolves to the `actual external` declaration
 * in `jsMain`, which has had its default arguments stripped and its mixin parameters widened to
 * `dynamic`. The index builds its own map of the `expect` declarations and answers from
 * that side whenever one exists.
 */
internal class DeclarationIndex(private val resolver: Resolver) {
    private val expects: Map<String, KSClassDeclaration> by lazy {
        resolver.getAllFiles()
            .flatMap(KSFile::declarations)
            .filterIsInstance<KSClassDeclaration>()
            .filter(KSClassDeclaration::isExpect)
            .mapNotNull { declaration -> declaration.qualifiedName?.asString()?.let { it to declaration } }
            .toMap()
    }

    private val resolved = mutableMapOf<String, KSClassDeclaration?>()

    /** The declaration for [qualifiedName], `expect` side preferred. Misses are cached as well. */
    fun declarationFor(qualifiedName: String): KSClassDeclaration? {
        if (qualifiedName in resolved) return resolved.getValue(qualifiedName)
        val declaration = expects[qualifiedName]
            ?: resolver.getClassDeclarationByName(resolver.getKSNameFromString(qualifiedName))
        resolved[qualifiedName] = declaration
        return declaration
    }

    /**
     * [findPortableParent] re-read through this index, so a parent found on the `jsMain` supertype
     * list is still returned as its `expect` declaration.
     */
    fun portableParent(declaration: KSClassDeclaration): KSClassDeclaration? {
        val parent = declaration.findPortableParent() ?: return null
        val qualifiedName = parent.qualifiedName?.asString() ?: return null
        return declarationFor(qualifiedName) ?: parent
    }

    /**
     * The top-level function named [qualifiedName] — the option dictionary factories — again with the
     * `expect` side preferred, since only it still records the factory's default arguments.
     */
    fun topLevelFunctionFor(qualifiedName: String): KSFunctionDeclaration? {
        val candidates = resolver
            .getFunctionDeclarationsByName(resolver.getKSNameFromString(qualifiedName), includeTopLevel = true)
            .toList()
        return candidates.firstOrNull(KSFunctionDeclaration::isExpect) ?: candidates.firstOrNull()
    }
}
