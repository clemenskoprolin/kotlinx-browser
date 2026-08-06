package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Element
import kotlinx.browser.dom.Node

private class DetachedNode : Node()

private class DetachedElement : Element()

internal actual fun newDetachedNode(): Node = DetachedNode()

internal actual fun newDetachedElement(): Element = DetachedElement()

/*
 * Nothing to trace: the generated JVM stub is inert. That the JVM actual's own default is used
 * rather than the expect's `definedExternally` placeholder is already proven by the call not
 * throwing.
 */
internal actual fun cloneNodeArgumentTraceMatches(): Boolean = true
