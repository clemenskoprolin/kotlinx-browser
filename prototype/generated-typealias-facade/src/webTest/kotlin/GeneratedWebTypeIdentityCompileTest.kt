import kotlinx.browser.JsAny as PortableJsAny
import kotlinx.browser.dom.HTMLButtonElement as PortableHTMLButtonElement
import kotlinx.browser.dom.HTMLDivElement as PortableHTMLDivElement
import kotlinx.browser.dom.Node as PortableNode
import kotlinx.browser.dom.Text as PortableText
import kotlinx.browser.dom.ValidityState as PortableValidityState
import kotlinx.browser.dom.events.EventTarget as PortableEventTarget
import org.w3c.dom.HTMLDivElement as BrowserHTMLDivElement
import org.w3c.dom.NodeList as BrowserNodeList
import org.w3c.dom.Text as BrowserText
import org.w3c.dom.events.EventTarget as BrowserEventTarget

private fun browserToPortable(value: BrowserHTMLDivElement): PortableHTMLDivElement = value

private fun portableToBrowser(value: PortableHTMLDivElement): BrowserHTMLDivElement = value

private fun browserDivAsPortableEventTarget(value: BrowserHTMLDivElement): PortableEventTarget = value

private fun portableDivAsBrowserEventTarget(value: PortableHTMLDivElement): BrowserEventTarget = value

private fun browserTextToPortable(value: BrowserText): PortableText = value

private fun portableTextToBrowser(value: PortableText): BrowserText = value

private fun browserToPortableJsAny(value: kotlin.js.JsAny): PortableJsAny = value

private fun portableToBrowserJsAny(value: PortableJsAny): kotlin.js.JsAny = value

// The facade members resolve to the browser members they alias, so their results stay browser types.
private fun usePortableMembers(
    button: PortableHTMLButtonElement,
    child: PortableHTMLDivElement,
): PortableNode {
    button.disabled = true
    button.id = "submit"
    button.setAttribute("type", "submit")
    return button.appendChild(child)
}

private fun portableValidity(button: PortableHTMLButtonElement): PortableValidityState = button.validity

private fun portableLabelsAsBrowserType(button: PortableHTMLButtonElement): BrowserNodeList = button.labels


