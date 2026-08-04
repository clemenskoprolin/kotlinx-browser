import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.browser.dom.*
import kotlinx.browser.dom.events.EventTarget

class SafePackageJvmTest {
    @Test
    fun completeMarkerHierarchyLoadsOnJvm() {
        val div: Element = TestDivElement()

        assertIs<EventTarget>(div)
    }

    @Test
    fun allAllowlistedClassifiersLoadFromTheSafePackage() {
        assertEquals(53, allowlistedClasses.size)

        allowlistedClasses.forEach { classifier ->
            assertEquals(classifier, Class.forName(classifier.name))
        }
    }
}

private class TestDivElement : HTMLDivElement()

private val allowlistedClasses: List<Class<*>> = listOf(
    EventTarget::class.java,
    Node::class.java,
    Element::class.java,
    HTMLElement::class.java,
    CharacterData::class.java,
    Text::class.java,
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
