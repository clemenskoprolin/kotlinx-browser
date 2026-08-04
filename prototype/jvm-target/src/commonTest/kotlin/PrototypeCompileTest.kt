import kotlin.test.Test
import kotlin.test.assertTrue
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.EventTarget

class PrototypeCompileTest {
    @Test
    fun commonHierarchyCompiles() {
        assertTrue(true)
    }
}

private fun asElement(value: HTMLDivElement): Element = value

private fun asEventTarget(value: Element): EventTarget = value
