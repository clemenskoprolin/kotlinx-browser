package kotlinx.browser.dom.events

public expect abstract class Event

public expect interface EventListener {
    public fun handleEvent(event: Event)
}

public expect abstract class EventTarget {
    public fun addEventListener(type: String, callback: EventListener?)
    public fun addEventListener(type: String, callback: ((Event) -> Unit)?)
    public fun removeEventListener(type: String, callback: EventListener?)
    public fun removeEventListener(type: String, callback: ((Event) -> Unit)?)
    public fun dispatchEvent(event: Event): Boolean
}

public expect abstract class UIEvent : Event
public expect abstract class FocusEvent : UIEvent
public expect abstract class MouseEvent : UIEvent
public expect abstract class WheelEvent : MouseEvent
public expect abstract class InputEvent : UIEvent
public expect abstract class KeyboardEvent : UIEvent
public expect abstract class CompositionEvent : UIEvent
