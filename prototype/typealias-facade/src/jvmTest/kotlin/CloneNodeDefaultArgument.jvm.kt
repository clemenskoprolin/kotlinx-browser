package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Node

private class DetachedNode : Node()

internal actual fun newDetachedNode(): Node = DetachedNode()

internal actual fun cloneNodeArgumentTraceMatches(): Boolean = true
