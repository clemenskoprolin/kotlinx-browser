# Portable DOM typealias facade prototype

A JVM-safe `kotlinx.browser.dom` facade over the existing `org.w3c` types, sized to the Compose HTML
allowlist (`EventTarget <- Node <- Element <- HTMLElement <- HTMLDivElement`, plus `NodeList`,
`ValidityState`, and the option dictionaries).

- `commonMain` — `expect` declarations, mirroring the browser signatures verbatim.
- `webMain` — `actual typealias` to `org.w3c`, shared by JS and Wasm/JS.
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
 member on JS and Wasm/JS and to the JVM stub's own defaults on JVM.