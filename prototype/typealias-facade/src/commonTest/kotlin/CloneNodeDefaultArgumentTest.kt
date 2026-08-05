package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Node
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal expect fun newDetachedNode(): Node

internal expect fun cloneNodeArgumentTraceMatches(): Boolean

class CloneNodeDefaultArgumentTest {
    @Test
    fun defaultedMemberIsCallableFromCommonCode() {
        val node = newDetachedNode()
        assertNotNull(node.cloneNode())
        assertNotNull(node.cloneNode(deep = false))
        assertNotNull(node.cloneNode(deep = true))
        assertTrue(cloneNodeArgumentTraceMatches())
    }
}
