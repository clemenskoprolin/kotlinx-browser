package kotlinx.browser.dom

public actual typealias Blob = org.w3c.files.Blob

internal actual fun sliceWithOptionalArguments(
    blob: Blob,
    start: Int?,
    end: Int?,
    contentType: String?,
    suppliedArguments: Int,
): Blob = when (suppliedArguments) {
    0 -> blob.slice()
    SLICE_START_ARGUMENT -> blob.slice(start = requireNotNull(start))
    SLICE_END_ARGUMENT -> blob.slice(end = requireNotNull(end))
    SLICE_CONTENT_TYPE_ARGUMENT -> blob.slice(contentType = requireNotNull(contentType))
    SLICE_START_ARGUMENT or SLICE_END_ARGUMENT ->
        blob.slice(start = requireNotNull(start), end = requireNotNull(end))
    SLICE_START_ARGUMENT or SLICE_CONTENT_TYPE_ARGUMENT ->
        blob.slice(start = requireNotNull(start), contentType = requireNotNull(contentType))
    SLICE_END_ARGUMENT or SLICE_CONTENT_TYPE_ARGUMENT ->
        blob.slice(end = requireNotNull(end), contentType = requireNotNull(contentType))
    SLICE_START_ARGUMENT or SLICE_END_ARGUMENT or SLICE_CONTENT_TYPE_ARGUMENT ->
        blob.slice(
            start = requireNotNull(start),
            end = requireNotNull(end),
            contentType = requireNotNull(contentType),
        )
    else -> error("Unsupported Blob.slice argument mask: $suppliedArguments")
}
