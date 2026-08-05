package kotlinx.browser.dom.defaultarguments

public actual interface UnionAudioTrackOrTextTrackOrVideoTrack

public actual interface TrackEventInit

public actual interface EventInit

public actual interface GetRootNodeOptions

@Suppress("UNUSED_PARAMETER")
public actual fun TrackEventInit(
    track: UnionAudioTrackOrTextTrackOrVideoTrack?,
    bubbles: Boolean?,
    cancelable: Boolean?,
    composed: Boolean?,
): TrackEventInit {
    check(track == null)
    check(bubbles == false)
    check(cancelable == false)
    check(composed == false)
    return JvmTrackEventInit
}

private object JvmTrackEventInit : TrackEventInit

@Suppress("UNUSED_PARAMETER")
public actual fun EventInit(
    bubbles: Boolean?,
    cancelable: Boolean?,
    composed: Boolean?,
): EventInit {
    check(bubbles == false)
    check(cancelable == false)
    check(composed == false)
    return JvmEventInit
}

private object JvmEventInit : EventInit

@Suppress("UNUSED_PARAMETER")
public actual fun GetRootNodeOptions(composed: Boolean?): GetRootNodeOptions {
    check(composed == false)
    return JvmGetRootNodeOptions
}

private object JvmGetRootNodeOptions : GetRootNodeOptions

public actual class RegularActualDefaultMember actual constructor() {
    actual fun value(enabled: Boolean): Boolean = enabled
}
