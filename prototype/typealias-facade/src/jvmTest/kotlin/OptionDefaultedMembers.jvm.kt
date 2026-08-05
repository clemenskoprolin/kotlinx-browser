package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Element

private class DetachedElement : Element()

internal actual fun newDetachedElement(): Element = DetachedElement()
