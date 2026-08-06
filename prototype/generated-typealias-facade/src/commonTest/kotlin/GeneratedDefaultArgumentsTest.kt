package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Element
import kotlinx.browser.dom.GetRootNodeOptions
import kotlinx.browser.dom.Node
import kotlinx.browser.dom.ScrollToOptions
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal expect fun newDetachedNode(): Node

internal expect fun newDetachedElement(): Element

internal expect fun cloneNodeArgumentTraceMatches(): Boolean

/**
 * The generated `expect` members repeat the browser defaults through the `definedExternally`
 * placeholder. These calls only compile if the defaults survived generation, and only run if each
 * target resolves them to something real: the browser member on web, the generated stub's own
 * default on JVM.
 */
class GeneratedDefaultArgumentsTest {
    @Test
    fun defaultedMemberIsCallableFromCommonCode() {
        val node = newDetachedNode()

        assertNotNull(node.cloneNode())
        assertNotNull(node.cloneNode(deep = false))
        assertNotNull(node.cloneNode(deep = true))
        assertTrue(cloneNodeArgumentTraceMatches())
    }

    @Test
    fun optionDictionaryDefaultIsAvailableOnTheExpectMember() {
        val node = newDetachedNode()

        assertNotNull(node.getRootNode())
        assertNotNull(node.getRootNode(GetRootNodeOptions()))
    }

    @Test
    fun scrollDefaultsAreAvailableOnTheExpectMember() {
        val element = newDetachedElement()

        element.scroll()
        element.scroll(ScrollToOptions())
        element.scrollTo()
        element.scrollTo(ScrollToOptions())
        element.scrollBy()
        element.scrollBy(ScrollToOptions())
    }
}
