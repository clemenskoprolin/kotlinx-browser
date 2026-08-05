package kotlinx.browser.dom

/**
 * Stands in for `kotlin.js.definedExternally` so an `expect` member can repeat the browser
 * signature verbatim, including its defaults.
 *
 * On web the member is actualized by a typealias to the external declaration, which carries the
 * real defaults, so this value is never read. On JVM the `actual` member declares its own defaults.
 */
public val definedExternally: Nothing
    get() = throw UnsupportedOperationException("definedExternally is never evaluated")
