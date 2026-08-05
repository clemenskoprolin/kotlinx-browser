package kotlinx.browser.dom

public actual interface GetRootNodeOptions

public actual interface ScrollToOptions

private object JvmGetRootNodeOptions : GetRootNodeOptions

private object JvmScrollToOptions : ScrollToOptions

@Suppress("UNUSED_PARAMETER")
public actual fun GetRootNodeOptions(composed: Boolean?): GetRootNodeOptions = JvmGetRootNodeOptions

@Suppress("UNUSED_PARAMETER")
public actual fun ScrollToOptions(left: Double?, top: Double?): ScrollToOptions = JvmScrollToOptions

internal actual fun cloneNodeWithoutDeep(node: Node): Node = node

@Suppress("UNUSED_PARAMETER")
internal actual fun cloneNodeWithDeep(node: Node, deep: Boolean): Node = node

internal actual fun getRootNodeWithoutOptions(node: Node): Node = node

@Suppress("UNUSED_PARAMETER")
internal actual fun getRootNodeWithOptions(node: Node, options: GetRootNodeOptions): Node = node

internal actual fun scrollWithoutOptions(element: Element): Unit = Unit

@Suppress("UNUSED_PARAMETER")
internal actual fun scrollWithOptions(element: Element, options: ScrollToOptions): Unit = Unit

internal actual fun scrollToWithoutOptions(element: Element): Unit = Unit

@Suppress("UNUSED_PARAMETER")
internal actual fun scrollToWithOptions(element: Element, options: ScrollToOptions): Unit = Unit

internal actual fun scrollByWithoutOptions(element: Element): Unit = Unit

@Suppress("UNUSED_PARAMETER")
internal actual fun scrollByWithOptions(element: Element, options: ScrollToOptions): Unit = Unit
