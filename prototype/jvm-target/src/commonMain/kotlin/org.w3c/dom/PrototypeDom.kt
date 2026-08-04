package org.w3c.dom

import org.w3c.dom.events.EventTarget

public expect abstract class Node : EventTarget

public expect abstract class Element : Node

public expect abstract class HTMLElement : Element

public expect abstract class HTMLDivElement : HTMLElement
