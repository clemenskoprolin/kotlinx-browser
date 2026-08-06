@file:Suppress("ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")

package kotlinx.browser.dom

import kotlinx.browser.dom.events.EventTarget

public actual abstract class Node : EventTarget() {
    actual open val nodeType: Short = 0
    actual open val nodeName: String = ""
    actual open val baseURI: String = ""
    actual open val isConnected: Boolean = false
    actual open val parentNode: Node? = null
    actual open val parentElement: Element? = null
    actual open val childNodes: NodeList = EmptyNodeList
    actual open val firstChild: Node? = null
    actual open val lastChild: Node? = null
    actual open val previousSibling: Node? = null
    actual open val nextSibling: Node? = null
    actual open var nodeValue: String? = null
    actual open var textContent: String? = null

    actual open fun cloneNode(deep: Boolean = false): Node = this

    actual fun getRootNode(options: GetRootNodeOptions = GetRootNodeOptions()): Node = this

    actual fun hasChildNodes(): Boolean = false

    actual fun normalize() = Unit

    actual fun isEqualNode(otherNode: Node?): Boolean = otherNode === this

    actual fun isSameNode(otherNode: Node?): Boolean = otherNode === this

    actual fun compareDocumentPosition(other: Node): Short = 0

    actual fun contains(other: Node?): Boolean = other === this

    actual fun lookupPrefix(namespace: String?): String? = null

    actual fun lookupNamespaceURI(prefix: String?): String? = null

    actual fun isDefaultNamespace(namespace: String?): Boolean = false

    actual fun insertBefore(node: Node, child: Node?): Node = node

    actual fun appendChild(node: Node): Node = node

    actual fun replaceChild(node: Node, child: Node): Node = child

    actual fun removeChild(child: Node): Node = child
}

public actual abstract class NodeList {
    actual abstract val length: Int

    actual fun item(index: Int): Node? = null
}

public actual abstract class Element : Node() {
    actual open val namespaceURI: String? = null
    actual open val prefix: String? = null
    actual open val localName: String = ""
    actual open val tagName: String = ""
    actual open var id: String = ""
    actual open var className: String = ""
    actual open var slot: String = ""
    actual open var scrollTop: Double = 0.0
    actual open var scrollLeft: Double = 0.0
    actual open val scrollWidth: Int = 0
    actual open val scrollHeight: Int = 0
    actual open val clientTop: Int = 0
    actual open val clientLeft: Int = 0
    actual open val clientWidth: Int = 0
    actual open val clientHeight: Int = 0
    actual open var innerHTML: String = ""
    actual open var outerHTML: String = ""

    actual fun hasAttributes(): Boolean = false

    actual fun getAttribute(qualifiedName: String): String? = null

    actual fun getAttributeNS(namespace: String?, localName: String): String? = null

    actual fun setAttribute(qualifiedName: String, value: String) = Unit

    actual fun setAttributeNS(namespace: String?, qualifiedName: String, value: String) = Unit

    actual fun removeAttribute(qualifiedName: String) = Unit

    actual fun removeAttributeNS(namespace: String?, localName: String) = Unit

    actual fun hasAttribute(qualifiedName: String): Boolean = false

    actual fun hasAttributeNS(namespace: String?, localName: String): Boolean = false

    actual fun closest(selectors: String): Element? = null

    actual fun matches(selectors: String): Boolean = false

    actual fun webkitMatchesSelector(selectors: String): Boolean = false

    actual fun insertAdjacentElement(where: String, element: Element): Element? = element

    actual fun insertAdjacentText(where: String, data: String) = Unit

    actual fun scrollIntoView() = Unit

    actual fun scrollIntoView(arg: Boolean) = Unit

    actual fun scroll(options: ScrollToOptions = ScrollToOptions()) = Unit

    actual fun scroll(x: Double, y: Double) = Unit

    actual fun scrollTo(options: ScrollToOptions = ScrollToOptions()) = Unit

    actual fun scrollTo(x: Double, y: Double) = Unit

    actual fun scrollBy(options: ScrollToOptions = ScrollToOptions()) = Unit

    actual fun scrollBy(x: Double, y: Double) = Unit

    actual fun insertAdjacentHTML(position: String, text: String) = Unit

    actual fun setPointerCapture(pointerId: Int) = Unit

    actual fun releasePointerCapture(pointerId: Int) = Unit

    actual fun hasPointerCapture(pointerId: Int): Boolean = false
}

public actual abstract class HTMLElement : Element() {
    actual open var title: String = ""
    actual open var lang: String = ""
    actual open var translate: Boolean = false
    actual open var dir: String = ""
    actual open var hidden: Boolean = false
    actual open var tabIndex: Int = 0
    actual open var accessKey: String = ""
    actual open val accessKeyLabel: String
        get() = accessKey
    actual open var draggable: Boolean = false
    actual open var spellcheck: Boolean = false
    actual open var innerText: String = ""
    actual open val offsetParent: Element? = null
    actual open val offsetTop: Int = 0
    actual open val offsetLeft: Int = 0
    actual open val offsetWidth: Int = 0
    actual open val offsetHeight: Int = 0

    actual fun click() = Unit

    actual fun focus() = Unit

    actual fun blur() = Unit

    actual fun forceSpellCheck() = Unit
}

public actual abstract class CharacterData : Node() {
    actual open var data: String = ""
    actual open val length: Int
        get() = data.length

    actual fun substringData(offset: Int, count: Int): String =
        data.substring(offset, minOf(offset + count, data.length))

    actual fun appendData(data: String) {
        this.data += data
    }

    actual fun insertData(offset: Int, data: String) {
        this.data = this.data.substring(0, offset) + data + this.data.substring(offset)
    }

    actual fun deleteData(offset: Int, count: Int) {
        replaceData(offset, count, "")
    }

    actual fun replaceData(offset: Int, count: Int, data: String) {
        val endIndex = minOf(offset + count, this.data.length)
        this.data = this.data.replaceRange(offset, endIndex, data)
    }
}

public actual open class Text : CharacterData() {
    actual open val wholeText: String
        get() = data
    actual open val previousElementSibling: Element? = null
    actual open val nextElementSibling: Element? = null

    actual fun splitText(offset: Int): Text {
        val remainder = Text().also { it.data = data.substring(offset) }
        data = data.substring(0, offset)
        return remainder
    }

    actual open fun before(vararg nodes: Node) = Unit

    actual open fun before(vararg nodes: String) = Unit

    actual open fun after(vararg nodes: Node) = Unit

    actual open fun after(vararg nodes: String) = Unit

    actual open fun replaceWith(vararg nodes: Node) = Unit

    actual open fun replaceWith(vararg nodes: String) = Unit

    actual open fun remove() = Unit
}

public actual abstract class ValidityState {
    actual open val valueMissing: Boolean = false
    actual open val typeMismatch: Boolean = false
    actual open val patternMismatch: Boolean = false
    actual open val tooLong: Boolean = false
    actual open val tooShort: Boolean = false
    actual open val rangeUnderflow: Boolean = false
    actual open val rangeOverflow: Boolean = false
    actual open val stepMismatch: Boolean = false
    actual open val badInput: Boolean = false
    actual open val customError: Boolean = false
    actual open val valid: Boolean = true
}

public actual abstract class HTMLMediaElement : HTMLElement()

public actual abstract class HTMLAnchorElement : HTMLElement()
public actual abstract class HTMLAreaElement : HTMLElement()
public actual abstract class HTMLAudioElement : HTMLMediaElement()
public actual abstract class HTMLBRElement : HTMLElement()
public actual abstract class HTMLButtonElement : HTMLElement() {
    actual open var autofocus: Boolean = false
    actual open var disabled: Boolean = false
    actual open val form: HTMLFormElement? = null
    actual open var formAction: String = ""
    actual open var formEnctype: String = ""
    actual open var formMethod: String = ""
    actual open var formNoValidate: Boolean = false
    actual open var formTarget: String = ""
    actual open var name: String = ""
    actual open var type: String = ""
    actual open var value: String = ""
    actual open val willValidate: Boolean = true
    actual open val validity: ValidityState = EmptyValidityState
    actual open val validationMessage: String = ""
    actual open val labels: NodeList = EmptyNodeList

    actual fun checkValidity(): Boolean = true

    actual fun reportValidity(): Boolean = true

    actual fun setCustomValidity(error: String) = Unit
}
public actual abstract class HTMLCanvasElement : HTMLElement()
public actual abstract class HTMLDataListElement : HTMLElement()
public actual abstract class HTMLDListElement : HTMLElement()
public actual abstract class HTMLDivElement : HTMLElement() {
    actual open var align: String = ""
}
public actual abstract class HTMLEmbedElement : HTMLElement()
public actual abstract class HTMLFieldSetElement : HTMLElement()
public actual abstract class HTMLFormElement : HTMLElement()
public actual abstract class HTMLHRElement : HTMLElement()
public actual abstract class HTMLHeadingElement : HTMLElement()
public actual abstract class HTMLIFrameElement : HTMLElement()
public actual abstract class HTMLImageElement : HTMLElement()
public actual abstract class HTMLInputElement : HTMLElement()
public actual abstract class HTMLLIElement : HTMLElement()
public actual abstract class HTMLLabelElement : HTMLElement()
public actual abstract class HTMLLegendElement : HTMLElement()
public actual abstract class HTMLMapElement : HTMLElement()
public actual abstract class HTMLMeterElement : HTMLElement()
public actual abstract class HTMLOListElement : HTMLElement()
public actual abstract class HTMLObjectElement : HTMLElement()
public actual abstract class HTMLOptGroupElement : HTMLElement()
public actual abstract class HTMLOptionElement : HTMLElement()
public actual abstract class HTMLOutputElement : HTMLElement()
public actual abstract class HTMLParagraphElement : HTMLElement()
public actual abstract class HTMLParamElement : HTMLElement()
public actual abstract class HTMLPictureElement : HTMLElement()
public actual abstract class HTMLPreElement : HTMLElement()
public actual abstract class HTMLProgressElement : HTMLElement()
public actual abstract class HTMLSelectElement : HTMLElement()
public actual abstract class HTMLSourceElement : HTMLElement()
public actual abstract class HTMLSpanElement : HTMLElement()
public actual abstract class HTMLStyleElement : HTMLElement()
public actual abstract class HTMLTableCaptionElement : HTMLElement()
public actual abstract class HTMLTableCellElement : HTMLElement()
public actual abstract class HTMLTableColElement : HTMLElement()
public actual abstract class HTMLTableElement : HTMLElement()
public actual abstract class HTMLTableRowElement : HTMLElement()
public actual abstract class HTMLTableSectionElement : HTMLElement()
public actual abstract class HTMLTextAreaElement : HTMLElement()
public actual abstract class HTMLTrackElement : HTMLElement()
public actual abstract class HTMLUListElement : HTMLElement()
public actual abstract class HTMLVideoElement : HTMLMediaElement()

private object EmptyNodeList : NodeList() {
    override val length: Int = 0
}

private object EmptyValidityState : ValidityState()
