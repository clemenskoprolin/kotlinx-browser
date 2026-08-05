@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package kotlinx.browser.dom

/** A Blob classifier added specifically for the optional-member facade experiment, see README. */
public expect open class Blob

public sealed interface OmittedOptionalArgument

private data object OmittedOptionalArgumentValue : OmittedOptionalArgument

internal const val SLICE_START_ARGUMENT: Int = 1
internal const val SLICE_END_ARGUMENT: Int = 2
internal const val SLICE_CONTENT_TYPE_ARGUMENT: Int = 4

public fun Blob.slice(): Blob =
    sliceWithOptionalArguments(this, null, null, null, 0)

public fun Blob.slice(start: Int): Blob =
    sliceWithOptionalArguments(this, start, null, null, SLICE_START_ARGUMENT)

public fun Blob.slice(
    __omittedStart: OmittedOptionalArgument = OmittedOptionalArgumentValue,
    end: Int,
): Blob = sliceWithOptionalArguments(this, null, end, null, SLICE_END_ARGUMENT)

public fun Blob.slice(
    __omittedStart: OmittedOptionalArgument = OmittedOptionalArgumentValue,
    __omittedEnd: OmittedOptionalArgument = OmittedOptionalArgumentValue,
    contentType: String,
): Blob = sliceWithOptionalArguments(this, null, null, contentType, SLICE_CONTENT_TYPE_ARGUMENT)

public fun Blob.slice(start: Int, end: Int): Blob =
    sliceWithOptionalArguments(
        this,
        start,
        end,
        null,
        SLICE_START_ARGUMENT or SLICE_END_ARGUMENT,
    )

public fun Blob.slice(
    start: Int,
    __omittedEnd: OmittedOptionalArgument = OmittedOptionalArgumentValue,
    contentType: String,
): Blob = sliceWithOptionalArguments(
    this,
    start,
    null,
    contentType,
    SLICE_START_ARGUMENT or SLICE_CONTENT_TYPE_ARGUMENT,
)

public fun Blob.slice(
    __omittedStart: OmittedOptionalArgument = OmittedOptionalArgumentValue,
    end: Int,
    contentType: String,
): Blob = sliceWithOptionalArguments(
    this,
    null,
    end,
    contentType,
    SLICE_END_ARGUMENT or SLICE_CONTENT_TYPE_ARGUMENT,
)

public fun Blob.slice(start: Int, end: Int, contentType: String): Blob =
    sliceWithOptionalArguments(
        this,
        start,
        end,
        contentType,
        SLICE_START_ARGUMENT or SLICE_END_ARGUMENT or SLICE_CONTENT_TYPE_ARGUMENT,
    )

internal expect fun sliceWithOptionalArguments(
    blob: Blob,
    start: Int?,
    end: Int?,
    contentType: String?,
    suppliedArguments: Int,
): Blob
