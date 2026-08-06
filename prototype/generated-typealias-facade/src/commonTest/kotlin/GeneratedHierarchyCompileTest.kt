import kotlinx.browser.JsAny
import kotlinx.browser.dom.Element
import kotlinx.browser.dom.HTMLButtonElement
import kotlinx.browser.dom.HTMLDivElement
import kotlinx.browser.dom.HTMLVideoElement
import kotlinx.browser.dom.Node
import kotlinx.browser.dom.NodeList
import kotlinx.browser.dom.Text
import kotlinx.browser.dom.ValidityState
import kotlinx.browser.dom.events.EventTarget

private fun acceptsJsAny(value: JsAny): JsAny = value

private fun divAsElement(value: HTMLDivElement): Element = value

private fun divAsEventTarget(value: HTMLDivElement): EventTarget = value

private fun textAsEventTarget(value: Text): EventTarget = value

private fun videoAsEventTarget(value: HTMLVideoElement): EventTarget = value

private fun divAsJsAny(value: HTMLDivElement): JsAny = acceptsJsAny(value)

private fun useNodeMembers(node: Node, child: Node): Node {
    node.nodeValue = "value"
    node.textContent = "content"
    node.normalize()
    node.isSameNode(child)
    return node.appendChild(child)
}

private fun useElementMembers(element: HTMLDivElement) {
    element.id = "root"
    element.title = "title"
    element.align = "center"
    element.setAttribute("role", "main")
    element.scrollTo(0.0, 10.0)
}

// Members the browser declares on the mixin interfaces the facade drops; they survive because the
// generator flattens them into the class that inherits them.
private fun useFlattenedMembers(element: Element, child: Node): NodeList {
    element.append(child)
    element.prepend("leading")
    element.querySelector("#root")
    element.remove()
    return element.querySelectorAll("div")
}

private fun useTextMembers(text: Text): Text {
    text.appendData("content")
    val siblings: Element? = text.previousElementSibling ?: text.nextElementSibling
    text.before(text)
    text.before("leading")
    text.after(text)
    text.after("trailing")
    text.replaceWith(text)
    text.replaceWith("replacement")
    text.remove()
    return text.splitText(text.length)
}

private fun useButtonMembers(button: HTMLButtonElement): ValidityState {
    button.disabled = true
    button.value = "submit"
    button.setCustomValidity("")
    button.labels.length
    button.labels.item(0)
    return button.validity
}
