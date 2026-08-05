package kotlinx.browser.dom

/** Option dictionaries referenced by defaulted DOM members. */
public expect interface GetRootNodeOptions

public expect interface ScrollToOptions

@Suppress("UNUSED_PARAMETER")
public expect fun GetRootNodeOptions(composed: Boolean? = false): GetRootNodeOptions

@Suppress("UNUSED_PARAMETER")
public expect fun ScrollToOptions(left: Double? = null, top: Double? = null): ScrollToOptions

/*
 * Members with defaults cannot be copied into an expect class actualized by a typealias. Matching
 * extension overloads retain the browser call shapes without weakening non-null parameter types.
 */
public fun Node.cloneNode(): Node = cloneNodeWithoutDeep(this)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public fun Node.cloneNode(deep: Boolean): Node = cloneNodeWithDeep(this, deep)

public fun Node.getRootNode(): Node = getRootNodeWithoutOptions(this)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public fun Node.getRootNode(options: GetRootNodeOptions): Node = getRootNodeWithOptions(this, options)

public fun Element.scroll(): Unit = scrollWithoutOptions(this)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public fun Element.scroll(options: ScrollToOptions): Unit = scrollWithOptions(this, options)

public fun Element.scrollTo(): Unit = scrollToWithoutOptions(this)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public fun Element.scrollTo(options: ScrollToOptions): Unit = scrollToWithOptions(this, options)

public fun Element.scrollBy(): Unit = scrollByWithoutOptions(this)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public fun Element.scrollBy(options: ScrollToOptions): Unit = scrollByWithOptions(this, options)

internal expect fun cloneNodeWithoutDeep(node: Node): Node

internal expect fun cloneNodeWithDeep(node: Node, deep: Boolean): Node

internal expect fun getRootNodeWithoutOptions(node: Node): Node

internal expect fun getRootNodeWithOptions(node: Node, options: GetRootNodeOptions): Node

internal expect fun scrollWithoutOptions(element: Element)

internal expect fun scrollWithOptions(element: Element, options: ScrollToOptions)

internal expect fun scrollToWithoutOptions(element: Element)

internal expect fun scrollToWithOptions(element: Element, options: ScrollToOptions)

internal expect fun scrollByWithoutOptions(element: Element)

internal expect fun scrollByWithOptions(element: Element, options: ScrollToOptions)
