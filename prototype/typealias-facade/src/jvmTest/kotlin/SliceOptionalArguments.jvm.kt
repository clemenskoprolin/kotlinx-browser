package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Blob

internal actual fun newDetachedBlob(): Blob = Blob()

internal actual fun sliceArgumentTraceMatches(): Boolean = true
