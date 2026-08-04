package org.w3c.dom

import org.w3c.dom.events.EventTarget

public actual abstract class Node : EventTarget()

public actual abstract class Element : Node()

public actual abstract class HTMLElement : Element()

public actual abstract class HTMLDivElement : HTMLElement()
