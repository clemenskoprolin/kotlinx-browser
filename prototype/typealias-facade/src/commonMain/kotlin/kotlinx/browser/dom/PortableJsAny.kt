package kotlinx.browser.dom

/**
 * The portable interop marker every facade classifier carries.
 *
 * Actualized per target rather than once for the web: on Wasm/JS it is the real `kotlin.js.JsAny`
 * interop classifier, but on JS that name is itself a typealias to `Any`, and Kotlin rejects an
 * `actual typealias` that resolves to another typealias. Pointing JS straight at `Any` says the same
 * thing without needing `ACTUAL_TYPE_ALIAS_NOT_TO_CLASS` suppressed.
 */
public expect interface JsAny
