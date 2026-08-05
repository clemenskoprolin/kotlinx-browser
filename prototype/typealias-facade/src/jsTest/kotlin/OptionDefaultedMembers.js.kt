package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Element
import kotlin.js.unsafeCast

internal actual fun newDetachedElement(): Element = js(
    "({ scroll: function(options) {}, scrollTo: function(options) {}, " +
        "scrollBy: function(options) {} })",
).unsafeCast<Element>()
