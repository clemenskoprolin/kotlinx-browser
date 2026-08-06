package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Element
import kotlinx.browser.dom.Node

internal actual fun newDetachedNode(): Node = js(
    "(globalThis.portableCloneNodeArguments = [], " +
        "globalThis.Node = function Node() {}, " +
        "globalThis.Node.prototype.cloneNode = function(deep) { " +
        "globalThis.portableCloneNodeArguments.push(deep); return this; }, " +
        "globalThis.Node.prototype.getRootNode = function(options) { return this; }, " +
        "new globalThis.Node())",
)

internal actual fun newDetachedElement(): Element = js(
    "({ scroll: function(options) {}, scrollTo: function(options) {}, " +
        "scrollBy: function(options) {} })",
)

internal actual fun cloneNodeArgumentTraceMatches(): Boolean = js(
    "globalThis.portableCloneNodeArguments.length === 3 && " +
        "globalThis.portableCloneNodeArguments[0] === undefined && " +
        "globalThis.portableCloneNodeArguments[1] === false && " +
        "globalThis.portableCloneNodeArguments[2] === true",
)
