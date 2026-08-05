package kotlinx.browser.dom

public actual typealias GetRootNodeOptions = org.w3c.dom.GetRootNodeOptions

public actual typealias ScrollToOptions = org.w3c.dom.ScrollToOptions

@Suppress("UNUSED_PARAMETER")
public actual fun GetRootNodeOptions(composed: Boolean?): GetRootNodeOptions =
    org.w3c.dom.GetRootNodeOptions(composed)

@Suppress("UNUSED_PARAMETER")
public actual fun ScrollToOptions(left: Double?, top: Double?): ScrollToOptions =
    org.w3c.dom.ScrollToOptions(left, top)

internal actual fun cloneNodeWithoutDeep(node: Node): Node = node.cloneNode()

internal actual fun cloneNodeWithDeep(node: Node, deep: Boolean): Node = node.cloneNode(deep)

internal actual fun getRootNodeWithoutOptions(node: Node): Node = node.getRootNode()

internal actual fun getRootNodeWithOptions(node: Node, options: GetRootNodeOptions): Node =
    node.getRootNode(options)

internal actual fun scrollWithoutOptions(element: Element): Unit = element.scroll()

internal actual fun scrollWithOptions(element: Element, options: ScrollToOptions): Unit =
    element.scroll(options)

internal actual fun scrollToWithoutOptions(element: Element): Unit = element.scrollTo()

internal actual fun scrollToWithOptions(element: Element, options: ScrollToOptions): Unit =
    element.scrollTo(options)

internal actual fun scrollByWithoutOptions(element: Element): Unit = element.scrollBy()

internal actual fun scrollByWithOptions(element: Element, options: ScrollToOptions): Unit =
    element.scrollBy(options)
