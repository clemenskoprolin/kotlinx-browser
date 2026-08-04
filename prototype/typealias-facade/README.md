# Portable DOM typealias facade prototype

This module tests the proposed facade architecture with the complete marker allowlist derived from
Compose HTML: 52 DOM classifiers plus the `EventTarget` dependency. Its core inheritance paths are:

```text
EventTarget <- Node <- Element <- HTMLElement <- HTMLDivElement
```

- `commonMain` declares marker-only `expect` classes in the JVM-safe `kotlinx.browser.dom` package.
- `jsMain` and `wasmJsMain` depend on the root kotlinx-browser project and use `actual typealias`
  declarations to its existing `org.w3c.dom` types.
- `jvmMain` supplies empty marker `actual` classes in the safe package.

The experiment intentionally excludes members, `JsAny`, and all event behavior.

Run it with:

```shell
./gradlew :typealias-facade-prototype:compileTestKotlinJs :typealias-facade-prototype:compileTestKotlinWasmJs :typealias-facade-prototype:jvmTest --offline
```

## Result

Verified on JDK 21 with Kotlin `2.2.20-Beta2`:

- All 52 DOM declarations actualize to the existing kotlinx-browser types on JS and Wasm/JS.
- Bidirectional checks cover both the abstract element chain and the open `Text` edge case.
- The mapped inheritance paths compile in common code.
- All 53 JVM marker classes load successfully with `Class.forName`.

This proves the type-only architecture for the complete allowlist. It does not cover DOM members.
