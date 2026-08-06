import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import kotlinx.browser.dom.*
import kotlinx.browser.dom.events.EventTarget

class SafePackageJvmTest {
    @Test
    fun completeMarkerHierarchyLoadsOnJvm() {
        val div: Element = TestDivElement()

        assertIs<EventTarget>(div)
        assertIs<JsAny>(div)
    }

    @Test
    fun classifiersOutsideTheEventTargetHierarchyAlsoCarryTheMarker() {
        val button = TestButtonElement()

        assertIs<JsAny>(button.labels)
        assertIs<JsAny>(button.validity)
        assertIs<JsAny>(GetRootNodeOptions())
        assertIs<JsAny>(ScrollToOptions())
    }

    @Test
    fun allAllowlistedClassifiersLoadFromTheSafePackage() {
        assertEquals(57, allowlistedClasses.size)

        allowlistedClasses.forEach { classifier ->
            assertEquals(classifier, Class.forName(classifier.name))
        }
    }

    @Test
    fun representativeMemberStubsAreUsableOnJvm() {
        val div = TestDivElement()
        div.id = "root"
        div.title = "Portable title"
        div.align = "center"
        div.innerHTML = "content"

        assertEquals("root", div.id)
        assertEquals("Portable title", div.title)
        assertEquals("center", div.align)
        assertEquals("content", div.innerHTML)
        assertFalse(div.hasAttributes())
        assertSame(div, div.appendChild(div))

        val text = Text()
        text.appendData("hello world")
        val remainder = text.splitText(5)

        assertEquals("hello", text.data)
        assertEquals(" world", remainder.wholeText)

        val button = TestButtonElement()
        button.disabled = true
        button.value = "submit"
        button.click()

        assertTrue(button.disabled)
        assertEquals("submit", button.value)
        assertTrue(button.checkValidity())
        assertTrue(button.validity.valid)
        assertEquals(0, button.labels.length)
        assertNull(button.labels.item(0))
    }
}

private class TestDivElement : HTMLDivElement()
private class TestButtonElement : HTMLButtonElement()

private val allowlistedClasses: List<Class<*>> = listOf(
    EventTarget::class.java,
    Node::class.java,
    NodeList::class.java,
    Element::class.java,
    HTMLElement::class.java,
    CharacterData::class.java,
    Text::class.java,
    ValidityState::class.java,
    GetRootNodeOptions::class.java,
    ScrollToOptions::class.java,
    HTMLMediaElement::class.java,
    HTMLAnchorElement::class.java,
    HTMLAreaElement::class.java,
    HTMLAudioElement::class.java,
    HTMLBRElement::class.java,
    HTMLButtonElement::class.java,
    HTMLCanvasElement::class.java,
    HTMLDataListElement::class.java,
    HTMLDListElement::class.java,
    HTMLDivElement::class.java,
    HTMLEmbedElement::class.java,
    HTMLFieldSetElement::class.java,
    HTMLFormElement::class.java,
    HTMLHRElement::class.java,
    HTMLHeadingElement::class.java,
    HTMLIFrameElement::class.java,
    HTMLImageElement::class.java,
    HTMLInputElement::class.java,
    HTMLLIElement::class.java,
    HTMLLabelElement::class.java,
    HTMLLegendElement::class.java,
    HTMLMapElement::class.java,
    HTMLMeterElement::class.java,
    HTMLOListElement::class.java,
    HTMLObjectElement::class.java,
    HTMLOptGroupElement::class.java,
    HTMLOptionElement::class.java,
    HTMLOutputElement::class.java,
    HTMLParagraphElement::class.java,
    HTMLParamElement::class.java,
    HTMLPictureElement::class.java,
    HTMLPreElement::class.java,
    HTMLProgressElement::class.java,
    HTMLSelectElement::class.java,
    HTMLSourceElement::class.java,
    HTMLSpanElement::class.java,
    HTMLStyleElement::class.java,
    HTMLTableCaptionElement::class.java,
    HTMLTableCellElement::class.java,
    HTMLTableColElement::class.java,
    HTMLTableElement::class.java,
    HTMLTableRowElement::class.java,
    HTMLTableSectionElement::class.java,
    HTMLTextAreaElement::class.java,
    HTMLTrackElement::class.java,
    HTMLUListElement::class.java,
    HTMLVideoElement::class.java,
)
