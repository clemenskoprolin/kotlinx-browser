package kotlinx.browser.dom.events

public actual external abstract class Event

public actual external interface EventListener {
    public actual fun handleEvent(event: Event)
}

public actual external abstract class EventTarget {
    public actual fun addEventListener(type: String, callback: EventListener?)
    public actual fun addEventListener(type: String, callback: ((Event) -> Unit)?)
    public actual fun removeEventListener(type: String, callback: EventListener?)
    public actual fun removeEventListener(type: String, callback: ((Event) -> Unit)?)
    public actual fun dispatchEvent(event: Event): Boolean
}

public actual external abstract class UIEvent : Event
public actual external abstract class FocusEvent : UIEvent
public actual external abstract class MouseEvent : UIEvent
public actual external abstract class WheelEvent : MouseEvent
public actual external abstract class InputEvent : UIEvent
public actual external abstract class KeyboardEvent : UIEvent
public actual external abstract class CompositionEvent : UIEvent
