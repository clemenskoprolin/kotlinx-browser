import kotlinx.browser.dom.Element
import kotlinx.browser.dom.HTMLButtonElement
import kotlinx.browser.dom.HTMLDivElement
import kotlinx.browser.dom.HTMLVideoElement
import kotlinx.browser.dom.Node
import kotlinx.browser.dom.Text
import kotlinx.browser.dom.ValidityState
import kotlinx.browser.dom.events.EventTarget

private fun asElement(value: HTMLDivElement): Element = value

private fun asEventTarget(value: Element): EventTarget = value

private fun textAsNode(value: Text): Node = value

private fun videoAsEventTarget(value: HTMLVideoElement): EventTarget = value

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
