package kotlinx.browser.dom

public actual interface GetRootNodeOptions : JsAny

public actual interface ScrollToOptions : JsAny

private object JvmGetRootNodeOptions : GetRootNodeOptions

private object JvmScrollToOptions : ScrollToOptions

public actual fun GetRootNodeOptions(composed: Boolean?): GetRootNodeOptions = JvmGetRootNodeOptions

public actual fun ScrollToOptions(left: Double?, top: Double?): ScrollToOptions = JvmScrollToOptions
