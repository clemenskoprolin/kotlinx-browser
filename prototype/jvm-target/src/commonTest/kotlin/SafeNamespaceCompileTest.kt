import kotlinx.browser.dom.Element
import kotlinx.browser.dom.HTMLCanvasElement
import kotlinx.browser.dom.HTMLDivElement
import kotlinx.browser.dom.HTMLVideoElement
import kotlinx.browser.dom.events.EventTarget

private fun divAsElement(value: HTMLDivElement): Element = value

private fun canvasAsElement(value: HTMLCanvasElement): Element = value

private fun videoAsEventTarget(value: HTMLVideoElement): EventTarget = value
