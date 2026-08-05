package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Element
import kotlinx.browser.dom.GetRootNodeOptions
import kotlinx.browser.dom.ScrollToOptions
import kotlin.test.Test
import kotlin.test.assertNotNull

internal expect fun newDetachedElement(): Element

class OptionDefaultedMembersTest {
    @Test
    fun getRootNodeDefaultIsAvailableOnTheExpectMember() {
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
