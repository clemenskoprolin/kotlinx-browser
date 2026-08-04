import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.browser.dom.Element
import kotlinx.browser.dom.HTMLDivElement
import kotlinx.browser.dom.events.Event
import kotlinx.browser.dom.events.EventTarget

class SafeNamespaceJvmTest {
    @Test
    fun markerHierarchyLoadsOnJvm() {
        val element: Element = TestSafeDivElement()
        assertIs<EventTarget>(element)
    }

    @Test
    fun eventOperationsAreNoOpsOnJvm() {
        val target = TestSafeDivElement()
        val event = TestSafeEvent()

        target.addEventListener("click") { error("JVM no-op listener must not run") }

        assertTrue(target.dispatchEvent(event))
    }
}

private class TestSafeDivElement : HTMLDivElement()

private class TestSafeEvent : Event()
