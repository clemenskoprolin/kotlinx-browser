package kotlinx.browser.dom

public actual typealias GetRootNodeOptions = org.w3c.dom.GetRootNodeOptions

public actual typealias ScrollToOptions = org.w3c.dom.ScrollToOptions

public actual fun GetRootNodeOptions(composed: Boolean?): GetRootNodeOptions =
    org.w3c.dom.GetRootNodeOptions(composed)

public actual fun ScrollToOptions(left: Double?, top: Double?): ScrollToOptions =
    org.w3c.dom.ScrollToOptions(left, top)
