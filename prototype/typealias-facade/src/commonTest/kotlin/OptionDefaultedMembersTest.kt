package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Element
import kotlinx.browser.dom.Node
import kotlinx.browser.dom.ScrollToOptions
import kotlinx.browser.dom.getRootNode
import kotlinx.browser.dom.scroll
import kotlinx.browser.dom.scrollBy
import kotlinx.browser.dom.scrollTo
import kotlin.test.Test
import kotlin.test.assertNotNull

internal expect fun newDetachedElement(): Element

class OptionDefaultedMembersTest {
    @Test
    fun getRootNodeDefaultWorksThroughCommonExtensionOverloads() {
        val node = newDetachedNode()
        assertNotNull(node.getRootNode())
        assertNotNull(node.getRootNode(kotlinx.browser.dom.GetRootNodeOptions()))
    }

    @Test
    fun scrollDefaultsWorkThroughCommonExtensionOverloads() {
        val element = newDetachedElement()
        element.scroll()
        element.scroll(ScrollToOptions())
        element.scrollTo()
        element.scrollTo(ScrollToOptions())
        element.scrollBy()
        element.scrollBy(ScrollToOptions())
    }
}
