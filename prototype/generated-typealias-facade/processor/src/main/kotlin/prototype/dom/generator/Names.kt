package prototype.dom.generator

import com.squareup.kotlinpoet.ClassName

// Packages we read from (the existing generated webMain sources) ...
internal const val DOM_PACKAGE = "org.w3c.dom"
internal const val EVENT_TARGET = "org.w3c.dom.events.EventTarget"

// ... and the packages we emit the portable facade into.
internal const val PORTABLE_JS_PACKAGE = "kotlinx.browser"
internal const val PORTABLE_DOM_PACKAGE = "kotlinx.browser.dom"
internal const val PORTABLE_EVENTS_PACKAGE = "kotlinx.browser.dom.events"

// Root folder under the KSP resource output where the four staged source sets are written.
internal const val STAGING_ROOT = "portableDom"

internal const val BROWSER_JS_ANY_ALIAS = "BrowserJsAny"

// Sanity bounds on the resolved model: if the upstream generator drifts, these stop generation
// instead of silently emitting a different facade. Seeds are the allowlisted types; the closure
// additionally contains the browser supertypes discovered through inheritance.
internal const val EXPECTED_SEED_COUNT = 47
internal const val EXPECTED_CLOSURE_COUNT = 53

internal val PORTABLE_JS_ANY = ClassName(PORTABLE_JS_PACKAGE, "JsAny")
internal val BROWSER_JS_ANY = ClassName("kotlin.js", "JsAny")
internal val ANY = ClassName("kotlin", "Any")
internal val PORTABLE_EVENT_TARGET = ClassName(PORTABLE_EVENTS_PACKAGE, "EventTarget")
