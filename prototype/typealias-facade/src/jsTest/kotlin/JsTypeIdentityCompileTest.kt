import kotlinx.browser.dom.HTMLDivElement as PortableHTMLDivElement
import kotlinx.browser.dom.Text as PortableText
import kotlinx.browser.dom.events.EventTarget as PortableEventTarget
import org.w3c.dom.HTMLDivElement as BrowserHTMLDivElement
import org.w3c.dom.Text as BrowserText
import org.w3c.dom.events.EventTarget as BrowserEventTarget

private fun browserToPortable(value: BrowserHTMLDivElement): PortableHTMLDivElement = value

private fun portableToBrowser(value: PortableHTMLDivElement): BrowserHTMLDivElement = value

private fun browserDivAsPortableEventTarget(value: BrowserHTMLDivElement): PortableEventTarget = value

private fun portableDivAsBrowserEventTarget(value: PortableHTMLDivElement): BrowserEventTarget = value

private fun browserTextToPortable(value: BrowserText): PortableText = value

private fun portableTextToBrowser(value: PortableText): BrowserText = value
