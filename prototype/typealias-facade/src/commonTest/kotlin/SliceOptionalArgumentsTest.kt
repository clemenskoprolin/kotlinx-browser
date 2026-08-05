package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Blob
import kotlinx.browser.dom.slice
import kotlin.test.Test
import kotlin.test.assertSame
import kotlin.test.assertTrue

internal expect fun newDetachedBlob(): Blob

internal expect fun sliceArgumentTraceMatches(): Boolean

class SliceOptionalArgumentsTest {
    @Test
    fun namedOptionalHolesWorkThroughGeneratedExtensionOverloads() {
        val blob = newDetachedBlob()
        val results = listOf(
            blob.slice(),
            blob.slice(10),
            blob.slice(end = 20),
            blob.slice(contentType = "content-only"),
            blob.slice(11, 21),
            blob.slice(start = 12, contentType = "start-content"),
            blob.slice(end = 22, contentType = "end-content"),
            blob.slice(13, 23, "all"),
        )

        results.forEach { assertSame(blob, it) }
        assertTrue(sliceArgumentTraceMatches())
    }
}
