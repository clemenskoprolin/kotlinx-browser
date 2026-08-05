package kotlinx.browser.dom

public actual interface GetRootNodeOptions

public actual interface ScrollToOptions

private object JvmGetRootNodeOptions : GetRootNodeOptions

private object JvmScrollToOptions : ScrollToOptions

public actual fun GetRootNodeOptions(composed: Boolean?): GetRootNodeOptions = JvmGetRootNodeOptions

public actual fun ScrollToOptions(left: Double?, top: Double?): ScrollToOptions = JvmScrollToOptions
