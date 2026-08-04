import kotlinx.browser.dom.Element
import kotlinx.browser.dom.HTMLDivElement
import kotlinx.browser.dom.HTMLVideoElement
import kotlinx.browser.dom.Node
import kotlinx.browser.dom.Text
import kotlinx.browser.dom.events.EventTarget

private fun asElement(value: HTMLDivElement): Element = value

private fun asEventTarget(value: Element): EventTarget = value

private fun textAsNode(value: Text): Node = value

private fun videoAsEventTarget(value: HTMLVideoElement): EventTarget = value
