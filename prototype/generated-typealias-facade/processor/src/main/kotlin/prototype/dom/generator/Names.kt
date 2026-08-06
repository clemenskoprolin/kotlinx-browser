package prototype.dom.generator

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.UNIT

// Packages we read from (the existing generated webMain sources) ...
internal const val DOM_PACKAGE = "org.w3c.dom"
internal const val EVENT_TARGET = "org.w3c.dom.events.EventTarget"

// ... and the packages we emit the portable facade into.
internal const val PORTABLE_JS_PACKAGE = "kotlinx.browser"
internal const val PORTABLE_DOM_PACKAGE = "kotlinx.browser.dom"
internal const val PORTABLE_EVENTS_PACKAGE = "kotlinx.browser.dom.events"

// Root folder under the KSP resource output where the staged source sets are written.
internal const val STAGING_ROOT = "portableDom"

internal const val BROWSER_JS_ANY_ALIAS = "BrowserJsAny"

// Sanity bounds on the resolved model: if the upstream generator drifts, these stop generation
// instead of silently emitting a different facade. Seeds are the allowlisted types; the closure
// additionally contains the browser supertypes discovered through inheritance.
internal const val EXPECTED_SEED_COUNT = 51
internal const val EXPECTED_CLOSURE_COUNT = 57

internal val PORTABLE_JS_ANY = ClassName(PORTABLE_JS_PACKAGE, "JsAny")
internal val BROWSER_JS_ANY = ClassName("kotlin.js", "JsAny")
internal val PORTABLE_EVENT_TARGET = ClassName(PORTABLE_EVENTS_PACKAGE, "EventTarget")

/**
 * Stand-in for `kotlin.js.definedExternally`, generated into `commonMain` so an `expect` member can
 * repeat the browser signature including its defaults..
 */
internal val DEFINED_EXTERNALLY = MemberName(PORTABLE_DOM_PACKAGE, "definedExternally")

/**
 * The Kotlin types a ported member may mention besides the facade classifiers themselves. Anything
 * else (`JsArray`, `Promise`, function types, the rest of the DOM) makes the member unportable.
 */
internal val BUILTIN_TYPES: Map<String, ClassName> =
    listOf(BOOLEAN, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, CHAR, STRING, UNIT)
        .associateBy(ClassName::canonicalName)

/** Supertypes that carry nothing portable and must not be followed when flattening. */
internal val IGNORED_SUPERTYPES = setOf("kotlin.Any", BROWSER_JS_ANY.canonicalName)

/*
 * Suppressed by internal name, Kotlin normally rejects default arguments on an `expect` member actualized
 * by a typealias, and checks the class scope of such an actual against the expect member by member.
 */
internal val WEB_SUPPRESSIONS = arrayOf(
    "DEFAULT_ARGUMENTS_IN_EXPECT_WITH_ACTUAL_TYPEALIAS",
    "EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE",
)

/** The JVM actuals supply the defaults that `definedExternally` stands in for. */
internal val JVM_SUPPRESSIONS = arrayOf("ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")

/** Backend-level check the frontend suppressions above do not cover */
internal const val IR_SUPPRESSION = "EXPECT_ACTUAL_IR_INCOMPATIBILITY"
