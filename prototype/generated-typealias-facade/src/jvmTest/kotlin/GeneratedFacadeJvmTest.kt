import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlinx.browser.JsAny
import kotlinx.browser.dom.*
import kotlinx.browser.dom.events.EventTarget

class GeneratedFacadeJvmTest {
    @Test
    fun generatedHierarchyLoadsInTheJvmSafePackage() {
        val div: Element = TestDivElement()

        assertIs<EventTarget>(div)
        assertIs<JsAny>(div)
    }

    @Test
    fun allFacadeClassifiersLoadFromTheSafePackage() {
        assertEquals(57, facadeClasses.size)

        facadeClasses.forEach { classifier ->
            assertEquals(classifier, Class.forName(classifier.name))
        }
    }

    /**
     * The generated JVM members are inert stubs, so the contract is the shape rather than the
     * behavior: properties hold what is written to them, and functions hand back an argument of the
     * right type, the receiver, or a manufactured empty value.
     */
    @Test
    fun generatedMemberStubsAreUsableOnJvm() {
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
        assertSame(div, div.removeChild(div))
        // Defaults come from the JVM actual, not from the `definedExternally` placeholder.
        assertSame(div, div.cloneNode())
        assertSame(div, div.getRootNode())
        assertNull(div.querySelector("#root"))
        assertEquals(0, div.querySelectorAll("div").length)

        val text = Text()
        text.data = "hello world"

        assertEquals("hello world", text.data)
        assertSame(text, text.splitText(5))

        val button = TestButtonElement()
        button.disabled = true
        button.value = "submit"
        button.click()

        assertEquals(true, button.disabled)
        assertEquals("submit", button.value)
        assertFalse(button.checkValidity())
        assertFalse(button.validity.valid)
        assertEquals(0, button.labels.length)
        assertNull(button.labels.item(0))
    }

    @Test
    fun optionDictionariesAreBuiltFromTheGeneratedFactories() {
        assertIs<GetRootNodeOptions>(GetRootNodeOptions())
        assertIs<ScrollToOptions>(ScrollToOptions(left = 0.0, top = 10.0))
    }
}

private class TestDivElement : HTMLDivElement()

private class TestButtonElement : HTMLButtonElement()

private val facadeClasses: List<Class<*>> = listOf(
    CharacterData::class.java,
    Element::class.java,
    EventTarget::class.java,
    GetRootNodeOptions::class.java,
    HTMLAnchorElement::class.java,
    HTMLAreaElement::class.java,
    HTMLAudioElement::class.java,
    HTMLBRElement::class.java,
    HTMLButtonElement::class.java,
    HTMLCanvasElement::class.java,
    HTMLDListElement::class.java,
    HTMLDataListElement::class.java,
    HTMLDivElement::class.java,
    HTMLElement::class.java,
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
    HTMLMediaElement::class.java,
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
    Node::class.java,
    NodeList::class.java,
    ScrollToOptions::class.java,
    Text::class.java,
    ValidityState::class.java,
)
