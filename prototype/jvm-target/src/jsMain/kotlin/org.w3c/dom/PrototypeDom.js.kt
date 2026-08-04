package org.w3c.dom

import org.w3c.dom.events.EventTarget

public actual external abstract class Node : EventTarget

public actual external abstract class Element : Node

public actual external abstract class HTMLElement : Element

public actual external abstract class HTMLDivElement : HTMLElement
