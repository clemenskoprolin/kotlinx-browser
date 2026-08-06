package kotlinx.browser.dom

import kotlinx.browser.dom.events.EventTarget

@Suppress("EXPECT_ACTUAL_IR_INCOMPATIBILITY")
public expect abstract class Node : EventTarget {
    open val nodeType: Short
    open val nodeName: String
    open val baseURI: String
    open val isConnected: Boolean
    open val parentNode: Node?
    open val parentElement: Element?
    open val childNodes: NodeList
    open val firstChild: Node?
    open val lastChild: Node?
    open val previousSibling: Node?
    open val nextSibling: Node?
    open var nodeValue: String?
    open var textContent: String?
    open fun cloneNode(deep: Boolean = definedExternally): Node
    fun getRootNode(options: GetRootNodeOptions = definedExternally): Node
    fun hasChildNodes(): Boolean
    fun normalize()
    fun isEqualNode(otherNode: Node?): Boolean
    fun isSameNode(otherNode: Node?): Boolean
    fun compareDocumentPosition(other: Node): Short
    fun contains(other: Node?): Boolean
    fun lookupPrefix(namespace: String?): String?
    fun lookupNamespaceURI(prefix: String?): String?
    fun isDefaultNamespace(namespace: String?): Boolean
    fun insertBefore(node: Node, child: Node?): Node
    fun appendChild(node: Node): Node
    fun replaceChild(node: Node, child: Node): Node
    fun removeChild(child: Node): Node
}

public expect abstract class NodeList : JsAny {
    abstract val length: Int

    fun item(index: Int): Node?
}

public expect abstract class Element : Node {
    open val namespaceURI: String?
    open val prefix: String?
    open val localName: String
    open val tagName: String
    open var id: String
    open var className: String
    open var slot: String
    open var scrollTop: Double
    open var scrollLeft: Double
    open val scrollWidth: Int
    open val scrollHeight: Int
    open val clientTop: Int
    open val clientLeft: Int
    open val clientWidth: Int
    open val clientHeight: Int
    open var innerHTML: String
    open var outerHTML: String

    fun hasAttributes(): Boolean
    fun getAttribute(qualifiedName: String): String?
    fun getAttributeNS(namespace: String?, localName: String): String?
    fun setAttribute(qualifiedName: String, value: String)
    fun setAttributeNS(namespace: String?, qualifiedName: String, value: String)
    fun removeAttribute(qualifiedName: String)
    fun removeAttributeNS(namespace: String?, localName: String)
    fun hasAttribute(qualifiedName: String): Boolean
    fun hasAttributeNS(namespace: String?, localName: String): Boolean
    fun closest(selectors: String): Element?
    fun matches(selectors: String): Boolean
    fun webkitMatchesSelector(selectors: String): Boolean
    fun insertAdjacentElement(where: String, element: Element): Element?
    fun insertAdjacentText(where: String, data: String)
    fun scrollIntoView()
    fun scrollIntoView(arg: Boolean)
    fun scroll(options: ScrollToOptions = definedExternally)
    fun scroll(x: Double, y: Double)
    fun scrollTo(options: ScrollToOptions = definedExternally)
    fun scrollTo(x: Double, y: Double)
    fun scrollBy(options: ScrollToOptions = definedExternally)
    fun scrollBy(x: Double, y: Double)
    fun insertAdjacentHTML(position: String, text: String)
    fun setPointerCapture(pointerId: Int)
    fun releasePointerCapture(pointerId: Int)
    fun hasPointerCapture(pointerId: Int): Boolean
}

public expect abstract class HTMLElement : Element {
    open var title: String
    open var lang: String
    open var translate: Boolean
    open var dir: String
    open var hidden: Boolean
    open var tabIndex: Int
    open var accessKey: String
    open val accessKeyLabel: String
    open var draggable: Boolean
    open var spellcheck: Boolean
    open var innerText: String
    open val offsetParent: Element?
    open val offsetTop: Int
    open val offsetLeft: Int
    open val offsetWidth: Int
    open val offsetHeight: Int

    fun click()
    fun focus()
    fun blur()
    fun forceSpellCheck()
}

public expect abstract class CharacterData : Node {
    open var data: String
    open val length: Int

    fun substringData(offset: Int, count: Int): String
    fun appendData(data: String)
    fun insertData(offset: Int, data: String)
    fun deleteData(offset: Int, count: Int)
    fun replaceData(offset: Int, count: Int, data: String)
}

public expect open class Text : CharacterData {
    open val wholeText: String
    open val previousElementSibling: Element?
    open val nextElementSibling: Element?

    fun splitText(offset: Int): Text
    open fun before(vararg nodes: Node)
    open fun before(vararg nodes: String)
    open fun after(vararg nodes: Node)
    open fun after(vararg nodes: String)
    open fun replaceWith(vararg nodes: Node)
    open fun replaceWith(vararg nodes: String)
    open fun remove()
}

public expect abstract class ValidityState : JsAny {
    open val valueMissing: Boolean
    open val typeMismatch: Boolean
    open val patternMismatch: Boolean
    open val tooLong: Boolean
    open val tooShort: Boolean
    open val rangeUnderflow: Boolean
    open val rangeOverflow: Boolean
    open val stepMismatch: Boolean
    open val badInput: Boolean
    open val customError: Boolean
    open val valid: Boolean
}

public expect abstract class HTMLMediaElement : HTMLElement

public expect abstract class HTMLAnchorElement : HTMLElement
public expect abstract class HTMLAreaElement : HTMLElement
public expect abstract class HTMLAudioElement : HTMLMediaElement
public expect abstract class HTMLBRElement : HTMLElement
public expect abstract class HTMLButtonElement : HTMLElement {
    open var autofocus: Boolean
    open var disabled: Boolean
    open val form: HTMLFormElement?
    open var formAction: String
    open var formEnctype: String
    open var formMethod: String
    open var formNoValidate: Boolean
    open var formTarget: String
    open var name: String
    open var type: String
    open var value: String
    open val willValidate: Boolean
    open val validity: ValidityState
    open val validationMessage: String
    open val labels: NodeList

    fun checkValidity(): Boolean
    fun reportValidity(): Boolean
    fun setCustomValidity(error: String)
}
public expect abstract class HTMLCanvasElement : HTMLElement
public expect abstract class HTMLDataListElement : HTMLElement
public expect abstract class HTMLDListElement : HTMLElement
public expect abstract class HTMLDivElement : HTMLElement {
    open var align: String
}
public expect abstract class HTMLEmbedElement : HTMLElement
public expect abstract class HTMLFieldSetElement : HTMLElement
public expect abstract class HTMLFormElement : HTMLElement
public expect abstract class HTMLHRElement : HTMLElement
public expect abstract class HTMLHeadingElement : HTMLElement
public expect abstract class HTMLIFrameElement : HTMLElement
public expect abstract class HTMLImageElement : HTMLElement
public expect abstract class HTMLInputElement : HTMLElement
public expect abstract class HTMLLIElement : HTMLElement
public expect abstract class HTMLLabelElement : HTMLElement
public expect abstract class HTMLLegendElement : HTMLElement
public expect abstract class HTMLMapElement : HTMLElement
public expect abstract class HTMLMeterElement : HTMLElement
public expect abstract class HTMLOListElement : HTMLElement
public expect abstract class HTMLObjectElement : HTMLElement
public expect abstract class HTMLOptGroupElement : HTMLElement
public expect abstract class HTMLOptionElement : HTMLElement
public expect abstract class HTMLOutputElement : HTMLElement
public expect abstract class HTMLParagraphElement : HTMLElement
public expect abstract class HTMLParamElement : HTMLElement
public expect abstract class HTMLPictureElement : HTMLElement
public expect abstract class HTMLPreElement : HTMLElement
public expect abstract class HTMLProgressElement : HTMLElement
public expect abstract class HTMLSelectElement : HTMLElement
public expect abstract class HTMLSourceElement : HTMLElement
public expect abstract class HTMLSpanElement : HTMLElement
public expect abstract class HTMLStyleElement : HTMLElement
public expect abstract class HTMLTableCaptionElement : HTMLElement
public expect abstract class HTMLTableCellElement : HTMLElement
public expect abstract class HTMLTableColElement : HTMLElement
public expect abstract class HTMLTableElement : HTMLElement
public expect abstract class HTMLTableRowElement : HTMLElement
public expect abstract class HTMLTableSectionElement : HTMLElement
public expect abstract class HTMLTextAreaElement : HTMLElement
public expect abstract class HTMLTrackElement : HTMLElement
public expect abstract class HTMLUListElement : HTMLElement
public expect abstract class HTMLVideoElement : HTMLMediaElement
