# Portable DOM typealias facade prototype

A JVM-safe `kotlinx.browser.dom` facade over the existing `org.w3c` types, sized to the Compose HTML
allowlist (`EventTarget <- Node <- Element <- HTMLElement <- HTMLDivElement`, plus `NodeList`,
`ValidityState`, and the option dictionaries).

- `commonMain` — `expect` declarations, mirroring the browser signatures verbatim.
- `webMain` — `actual typealias` to `org.w3c`, shared by JS and Wasm/JS.
- `jsMain` / `wasmJsMain` — the `JsAny` marker, the one declaration that has to differ per target.
- `jvmMain` — `actual` classes with member stubs.

```shell
./gradlew :typealias-facade-prototype:jsNodeTest :typealias-facade-prototype:wasmJsNodeTest :typealias-facade-prototype:jvmTest
```

Kotlin normally rejects default arguments on an `expect` member actualized by a typealias.

We supress that:

| Diagnostic | Where         |
| --- |---------------|
| `DEFAULT_ARGUMENTS_IN_EXPECT_WITH_ACTUAL_TYPEALIAS`, `EXPECT_ACTUAL_INCOMPATIBLE_CLASS_SCOPE` | `webMain`     |
| `ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS` | `jvmMain`     |
| `EXPECT_ACTUAL_IR_INCOMPATIBILITY` | `commonMain`  |

## `JsAny`

Every facade classifier carries a portable `JsAny` interop marker. It is actualized per target
rather than once for the web:

| Target | Actual | Reason |
| --- | --- | --- |
| JS | `actual typealias JsAny = Any` | `kotlin.js.JsAny` is itself a typealias on JS, and Kotlin rejects an `actual typealias` that resolves to another typealias |
| Wasm/JS | `actual typealias JsAny = kotlin.js.JsAny` | Wasm exposes the real JS interop classifier |
| JVM | `actual interface JsAny` | Type-only server marker with no browser behavior |

Aliasing the shared `webMain` declaration straight to `kotlin.js.JsAny` also works, but only with
`ACTUAL_TYPE_ALIAS_NOT_TO_CLASS` suppressed. Splitting JS and Wasm/JS removes that suppression — one
fewer place where the compiler behavior is unspecified.

## Result

Verified on JDK 21 with Kotlin `2.2.20-Beta2`: all targets compile and pass, defaults resolve to the
underlying browser member on JS and Wasm/JS and to the JVM stub's own defaults on JVM.