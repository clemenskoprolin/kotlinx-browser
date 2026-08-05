package kotlinx.browser.dom

/** Option dictionaries referenced by defaulted DOM members. */
public expect interface GetRootNodeOptions

public expect interface ScrollToOptions

/*
 * Top-level factories are actualized independently of any typealias, so they keep their defaults on
 * the common `expect` declaration without needing a suppression.
 */
public expect fun GetRootNodeOptions(composed: Boolean? = false): GetRootNodeOptions

public expect fun ScrollToOptions(left: Double? = null, top: Double? = null): ScrollToOptions
