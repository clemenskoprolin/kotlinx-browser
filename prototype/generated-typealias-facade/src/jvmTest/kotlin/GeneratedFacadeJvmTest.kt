import kotlin.test.Test
import kotlin.test.assertIs
import kotlinx.browser.JsAny
import kotlinx.browser.dom.Element
import kotlinx.browser.dom.HTMLDivElement
import kotlinx.browser.dom.events.EventTarget

class GeneratedFacadeJvmTest {
    @Test
    fun generatedHierarchyLoadsInTheJvmSafePackage() {
        val div: Element = TestDivElement()

        assertIs<EventTarget>(div)
        assertIs<JsAny>(div)
    }
}

private class TestDivElement : HTMLDivElement()
