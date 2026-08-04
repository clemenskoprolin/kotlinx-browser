package kotlinx.browser.dom.events

public actual abstract class Event

public actual interface EventListener {
    public actual fun handleEvent(event: Event)
}

public actual abstract class EventTarget {
    public actual fun addEventListener(type: String, callback: EventListener?) = Unit
    public actual fun addEventListener(type: String, callback: ((Event) -> Unit)?) = Unit
    public actual fun removeEventListener(type: String, callback: EventListener?) = Unit
    public actual fun removeEventListener(type: String, callback: ((Event) -> Unit)?) = Unit
    public actual fun dispatchEvent(event: Event): Boolean = true
}

public actual abstract class UIEvent : Event()
public actual abstract class FocusEvent : UIEvent()
public actual abstract class MouseEvent : UIEvent()
public actual abstract class WheelEvent : MouseEvent()
public actual abstract class InputEvent : UIEvent()
public actual abstract class KeyboardEvent : UIEvent()
public actual abstract class CompositionEvent : UIEvent()
