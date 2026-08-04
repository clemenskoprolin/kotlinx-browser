package kotlinx.browser.dom

import kotlinx.browser.dom.events.EventTarget

public expect abstract class Node : EventTarget
public expect abstract class Element : Node
public expect abstract class HTMLElement : Element
public expect abstract class CharacterData : Node
public expect open class Text : CharacterData
public expect abstract class HTMLMediaElement : HTMLElement

public expect abstract class HTMLAnchorElement : HTMLElement
public expect abstract class HTMLAreaElement : HTMLElement
public expect abstract class HTMLAudioElement : HTMLMediaElement
public expect abstract class HTMLBRElement : HTMLElement
public expect abstract class HTMLButtonElement : HTMLElement
public expect abstract class HTMLCanvasElement : HTMLElement
public expect abstract class HTMLDataListElement : HTMLElement
public expect abstract class HTMLDListElement : HTMLElement
public expect abstract class HTMLDivElement : HTMLElement
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
