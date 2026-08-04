import kotlinx.browser.JsAny
import kotlinx.browser.dom.Element
import kotlinx.browser.dom.HTMLDivElement
import kotlinx.browser.dom.Text
import kotlinx.browser.dom.events.EventTarget

private fun acceptsJsAny(value: JsAny): JsAny = value

private fun divAsElement(value: HTMLDivElement): Element = value

private fun divAsEventTarget(value: HTMLDivElement): EventTarget = value

private fun textAsEventTarget(value: Text): EventTarget = value

private fun divAsJsAny(value: HTMLDivElement): JsAny = acceptsJsAny(value)
