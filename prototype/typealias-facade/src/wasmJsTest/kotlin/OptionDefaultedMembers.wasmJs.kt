@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Element

internal actual fun newDetachedElement(): Element = js(
    "({ scroll: function(options) {}, scrollTo: function(options) {}, " +
        "scrollBy: function(options) {} })",
)
