@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package kotlinx.browser.dom.defaultarguments

import kotlinx.browser.dom.Blob

internal actual fun newDetachedBlob(): Blob = js(
    "(globalThis.portableSliceArguments = [], " +
        "{ slice: function() { " +
        "globalThis.portableSliceArguments.push(Array.from(arguments)); return this; } })",
)

internal actual fun sliceArgumentTraceMatches(): Boolean = js(
    "globalThis.portableSliceArguments.length === 8 && " +
        "globalThis.portableSliceArguments[0].length === 3 && " +
        "globalThis.portableSliceArguments[0][0] === undefined && " +
        "globalThis.portableSliceArguments[0][1] === undefined && " +
        "globalThis.portableSliceArguments[0][2] === undefined && " +
        "globalThis.portableSliceArguments[1].length === 3 && " +
        "globalThis.portableSliceArguments[1][0] === 10 && " +
        "globalThis.portableSliceArguments[1][1] === undefined && " +
        "globalThis.portableSliceArguments[1][2] === undefined && " +
        "globalThis.portableSliceArguments[2].length === 3 && " +
        "globalThis.portableSliceArguments[2][0] === undefined && " +
        "globalThis.portableSliceArguments[2][1] === 20 && " +
        "globalThis.portableSliceArguments[2][2] === undefined && " +
        "globalThis.portableSliceArguments[3].length === 3 && " +
        "globalThis.portableSliceArguments[3][0] === undefined && " +
        "globalThis.portableSliceArguments[3][1] === undefined && " +
        "globalThis.portableSliceArguments[3][2] === 'content-only' && " +
        "globalThis.portableSliceArguments[4].length === 3 && " +
        "globalThis.portableSliceArguments[4][0] === 11 && " +
        "globalThis.portableSliceArguments[4][1] === 21 && " +
        "globalThis.portableSliceArguments[4][2] === undefined && " +
        "globalThis.portableSliceArguments[5].length === 3 && " +
        "globalThis.portableSliceArguments[5][0] === 12 && " +
        "globalThis.portableSliceArguments[5][1] === undefined && " +
        "globalThis.portableSliceArguments[5][2] === 'start-content' && " +
        "globalThis.portableSliceArguments[6].length === 3 && " +
        "globalThis.portableSliceArguments[6][0] === undefined && " +
        "globalThis.portableSliceArguments[6][1] === 22 && " +
        "globalThis.portableSliceArguments[6][2] === 'end-content' && " +
        "globalThis.portableSliceArguments[7].length === 3 && " +
        "globalThis.portableSliceArguments[7][0] === 13 && " +
        "globalThis.portableSliceArguments[7][1] === 23 && " +
        "globalThis.portableSliceArguments[7][2] === 'all'",
)
