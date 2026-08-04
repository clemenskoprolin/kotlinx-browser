import kotlinx.browser.JsAny as PortableJsAny
import kotlinx.browser.dom.HTMLDivElement as PortableHTMLDivElement
import org.w3c.dom.HTMLDivElement as BrowserHTMLDivElement

private fun browserToPortable(value: BrowserHTMLDivElement): PortableHTMLDivElement = value

private fun portableToBrowser(value: PortableHTMLDivElement): BrowserHTMLDivElement = value

private fun browserToPortableJsAny(value: kotlin.js.JsAny): PortableJsAny = value

private fun portableToBrowserJsAny(value: PortableJsAny): kotlin.js.JsAny = value
