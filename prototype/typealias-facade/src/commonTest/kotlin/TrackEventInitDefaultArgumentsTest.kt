package kotlinx.browser.dom.defaultarguments

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

public expect interface UnionAudioTrackOrTextTrackOrVideoTrack

public expect interface TrackEventInit

public expect interface EventInit

public expect interface GetRootNodeOptions

@Suppress("UNUSED_PARAMETER")
public expect fun TrackEventInit(
    track: UnionAudioTrackOrTextTrackOrVideoTrack? = null,
    bubbles: Boolean? = false,
    cancelable: Boolean? = false,
    composed: Boolean? = false,
): TrackEventInit

@Suppress("UNUSED_PARAMETER")
public expect fun EventInit(
    bubbles: Boolean? = false,
    cancelable: Boolean? = false,
    composed: Boolean? = false,
): EventInit

@Suppress("UNUSED_PARAMETER")
public expect fun GetRootNodeOptions(composed: Boolean? = false): GetRootNodeOptions

public expect class RegularActualDefaultMember() {
    fun value(enabled: Boolean = false): Boolean
}

class TrackEventInitDefaultArgumentsTest {
    @Test
    fun topLevelExpectFactoryCanCopyDefaultArguments() {
        assertNotNull(TrackEventInit())
        assertNotNull(EventInit())
        assertNotNull(GetRootNodeOptions())
    }

    @Test
    fun memberDefaultWorksWithAnOrdinaryActualClass() {
        assertFalse(RegularActualDefaultMember().value())
    }
}
