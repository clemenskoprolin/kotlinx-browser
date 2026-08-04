# Generated portable DOM facade prototype

This module generates the working typealias facade instead of checking handwritten DOM
declarations. It uses KSP to inspect the existing generated `webMain` declarations and KotlinPoet
to emit the portable common, JS, Wasm/JS, and JVM source sets.

## Result

The prototype is successful for the marker-only Compose HTML surface:

- JS and Wasm/JS retain type identity with the existing `org.w3c.dom` declarations through
  `actual typealias`.
- JVM gets loadable marker types under the safe `kotlinx.browser` namespace.

The generated files live under `build/generated/portableDom`

## Pipeline

```text
generated src/webMain declarations
              |
              v
       root :kspKotlinJs
              |
       KSP symbol model
       + inheritance closure
              |
       KotlinPoet source text
              |
   KSP generated resources (*.kt.txt)
              |
     generatePortableDomFacade
              |
 commonMain / jsMain / wasmJsMain / jvmMain
```

KSP creates output for a particular target compilation. The
processor writes the four source trees as staged KSP resources. A Gradle `Sync` task
materializes them as `.kt` files and the prototype registers those directories as generated source
roots. This avoids writing into `src` during compilation or requiring a second build.

The explicit seeds are in
[`processor/src/main/resources/compose-html-dom-allowlist.txt`](processor/src/main/resources/compose-html-dom-allowlist.txt).
The processor fails generation unless all seeds resolve from source and the expected declaration
closure is present.

## `JsAny`

The safe common API contains:

```kotlin
expect interface JsAny
```

Its generated actuals are intentionally different:

| Target | Actual | Reason |
| --- | --- | --- |
| JS | `actual typealias JsAny = Any` | On JS, `kotlin.js.JsAny` is itself a typealias; Kotlin rejects a chained `actual typealias` |
| Wasm/JS | `actual typealias JsAny = kotlin.js.JsAny` | Wasm exposes the real JS interop classifier |
| JVM | `actual interface JsAny` | Type-only server marker with no browser behavior |

Every generated DOM marker directly extends the portable `JsAny`. Compile tests prove conversions
between the portable and browser types on both web targets, while the JVM test proves that the safe
hierarchy loads and implements the marker.

## Run

After dependencies have been resolved once:

```shell
./gradlew :generated-typealias-facade-prototype:compileTestKotlinJs \
  :generated-typealias-facade-prototype:compileTestKotlinWasmJs \
  :generated-typealias-facade-prototype:jvmTest --offline
```

Inspect the resolved model at:

```text
prototype/generated-typealias-facade/build/generated/portableDom/model.txt
```
