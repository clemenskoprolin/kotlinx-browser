package kotlinx.browser.dom

public actual open class Blob

internal actual fun sliceWithOptionalArguments(
    blob: Blob,
    start: Int?,
    end: Int?,
    contentType: String?,
    suppliedArguments: Int,
): Blob {
    require(suppliedArguments in 0..7) {
        "Unsupported Blob.slice argument mask: $suppliedArguments"
    }
    if (suppliedArguments and SLICE_START_ARGUMENT != 0) requireNotNull(start)
    if (suppliedArguments and SLICE_END_ARGUMENT != 0) requireNotNull(end)
    if (suppliedArguments and SLICE_CONTENT_TYPE_ARGUMENT != 0) requireNotNull(contentType)
    return blob
}
