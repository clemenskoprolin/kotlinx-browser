# Generated portable DOM facade prototype

This module generates the working typealias facade instead of checking handwritten DOM
declarations. It uses KSP to inspect the existing generated `webMain` declarations and KotlinPoet
to emit the portable common, web, JS, Wasm/JS, and JVM source sets.

## Result

The prototype reproduces the handwritten [`typealias-facade`](../typealias-facade/) surface without
the handwriting: 57 classifiers with 566 members, generated from 51 seeds.

The generated files live under `build/generated/portableDom`.

## Pipeline

```text
generated src/webMain declarations
              |
              v
       root :kspKotlinJs
              |
       KSP symbol model
       + inheritance closure
       + member scan
              |
       KotlinPoet source text
              |
   KSP generated resources (*.kt.txt)
              |
     generatePortableDomFacade
              |
 commonMain / webMain / jsMain / wasmJsMain / jvmMain
```

KSP creates output for a particular target compilation. The processor writes the source trees as
staged KSP resources. A Gradle `Sync` task materializes them as `.kt` files and the prototype
registers those directories as generated source roots. This avoids writing into `src` during
compilation or requiring a second build.

## What gets generated

The explicit seeds are in
[`processor/src/main/resources/compose-html-dom-allowlist.txt`](processor/src/main/resources/compose-html-dom-allowlist.txt).
The processor fails generation unless all seeds resolve from source and the expected declaration
closure is present.

**Classifiers** are the inheritance closure of the seeds. Only the single class hierarchy is
modelled, so `superclass`/`superinterface` can be assigned correctly on each target.

**Members** are ported when every type in the signature is either a Kotlin builtin or one of the
resolved classifiers.

**Mixin interfaces** (`ParentNode`, `ChildNode`, `ItemArrayLike`, `ElementContentEditable`, ...) are
not part of the facade, so their members are flattened into the class that first inherits them.

**Option dictionaries** stay opaque. The browser builds them from a top-level factory, so the
interface is emitted member-free and the factory is ported alongside it.

**JVM bodies** are stups: a property initializes to an empty value of its type. A
function returns an argument of the matching type, then the receiver when the class is an instance
of a non-null return type, and otherwise a manufactured value. Where a non-null facade type has no 
such value, the generator emits a private singleton for it (`EmptyNodeList`, `EmptyValidityState`).

## `JsAny`

The common API contains:

```kotlin
expect interface JsAny
```

Its generated actuals are intentionally different:

| Target | Actual | Reason |
| --- | --- | --- |
| JS | `actual typealias JsAny = Any` | On JS, `kotlin.js.JsAny` is itself a typealias; Kotlin rejects a chained `actual typealias` |
| Wasm/JS | `actual typealias JsAny = kotlin.js.JsAny` | Wasm exposes the real JS interop classifier |
| JVM | `actual interface JsAny` | Type-only server marker with no browser behavior |

## Run

After dependencies have been resolved once:

```shell
./gradlew :generated-typealias-facade-prototype:jsNodeTest \
  :generated-typealias-facade-prototype:wasmJsNodeTest \
  :generated-typealias-facade-prototype:jvmTest --offline
```

Inspect the resolved model — classifiers, shapes and every ported member — at:

```text
prototype/generated-typealias-facade/build/generated/portableDom/model.txt
```
