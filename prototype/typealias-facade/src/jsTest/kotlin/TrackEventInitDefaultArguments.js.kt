package kotlinx.browser.dom.defaultarguments

public actual typealias UnionAudioTrackOrTextTrackOrVideoTrack =
    org.w3c.dom.UnionAudioTrackOrTextTrackOrVideoTrack

public actual typealias TrackEventInit = org.w3c.dom.TrackEventInit

public actual typealias EventInit = org.w3c.dom.EventInit

public actual typealias GetRootNodeOptions = org.w3c.dom.GetRootNodeOptions

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
    return org.w3c.dom.TrackEventInit(track, bubbles, cancelable, composed)
}

@Suppress("UNUSED_PARAMETER")
public actual fun EventInit(
    bubbles: Boolean?,
    cancelable: Boolean?,
    composed: Boolean?,
): EventInit {
    check(bubbles == false)
    check(cancelable == false)
    check(composed == false)
    return org.w3c.dom.EventInit(bubbles, cancelable, composed)
}

@Suppress("UNUSED_PARAMETER")
public actual fun GetRootNodeOptions(composed: Boolean?): GetRootNodeOptions {
    check(composed == false)
    return org.w3c.dom.GetRootNodeOptions(composed)
}

public actual class RegularActualDefaultMember actual constructor() {
    actual fun value(enabled: Boolean): Boolean = enabled
}
