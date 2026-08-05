# Portable DOM typealias facade prototype

This module tests the proposed facade architecture with the complete marker allowlist derived from
Compose HTML: some DOM classifiers plus the `EventTarget` dependency. `NodeList` and `ValidityState`
are included as the first types pulled in by ordinary member signatures; `GetRootNodeOptions` and
`ScrollToOptions` support members with defaulted option arguments. `Blob` is included as an
additional stress case for a member with several independently optional arguments. Its core
inheritance paths are:

```text
EventTarget <- Node <- Element <- HTMLElement <- HTMLDivElement
```

- `commonMain` declares portable `expect` classes in the JVM-safe `kotlinx.browser.dom` package.
- `jsMain` and `wasmJsMain` depend on the root kotlinx-browser project and use `actual typealias`
  declarations to its existing `org.w3c.dom` and `org.w3c.files` types.
- `jvmMain` supplies marker classes and member stubs in the safe package.

Run it with:

```shell
./gradlew :typealias-facade-prototype:jsNodeTest :typealias-facade-prototype:wasmJsNodeTest :typealias-facade-prototype:jvmTest --offline
```

## Result

Verified on JDK 21 with Kotlin `2.2.20-Beta2`:

- All DOM declarations actualize to the existing kotlinx-browser types on JS and Wasm/JS.
- Bidirectional checks cover both the abstract element chain and the open `Text` edge case.
- The mapped inheritance paths compile in common code.
- All JVM facade classes load successfully with `Class.forName`.

## Optional parameters
A member such as `cloneNode`, whose original
declaration has a default argument, cannot be repeated in an `expect` class actualized by a
typealias. Omitting the default makes the original actual member incompatible, while copying the
default is explicitly rejected for an expect class actualized by a typealias.

Something like this would work, default is declared only in commonMain:
```kotlin
// commonMain
expect class Node {
    fun cloneNode(deep: Boolean = false): Node
}

// jvmMain
actual class Node {
    actual fun cloneNode(deep: Boolean): Node = this
}
```

This does not work:
```kotlin
// commonMain
expect class Node {
    fun cloneNode(deep: Boolean = false): Node
}

// webMain
actual typealias Node = BrowserNode

// But BrowserNode already contains:
fun cloneNode(deep: Boolean = definedExternally): BrowserNode
```

The workaround keeps those functions outside the typealiased class and exposes ordinary
common extension overloads with their original names:

```kotlin
public fun Node.cloneNode(): Node = cloneNodeWithoutDeep(this)
public fun Node.cloneNode(deep: Boolean): Node = cloneNodeWithDeep(this, deep)

internal expect fun cloneNodeWithoutDeep(node: Node): Node
internal expect fun cloneNodeWithDeep(node: Node, deep: Boolean): Node
```

Top-level factory functions such as `ScrollToOptions(...)` do not have the same typealias-member
restriction: the function itself is actualized independently of the typealias, so its defaults can
remain on the common `expect` factory while each platform supplies the factory implementation.

## Multiple optional parameters: `Blob.slice`

The original browser member has three `definedExternally` defaults:

```kotlin
fun slice(
    start: Int = definedExternally,
    end: Int = definedExternally,
    contentType: String = definedExternally,
): Blob
```

Generating with the previous method would lose calls such as
`blob.slice(end = 100)`. Simply adding `fun Blob.slice(end: Int)` is also impossible because Kotlin
does not distinguish it from `fun Blob.slice(start: Int)` by parameter name.

We insert an unconstructible, defaulted marker in the position of every
omitted parameter that precedes a supplied one:

```kotlin
public sealed interface OmittedOptionalArgument
private data object OmittedOptionalArgumentValue : OmittedOptionalArgument

public fun Blob.slice(start: Int): Blob = // ...

public fun Blob.slice(
    __omittedStart: OmittedOptionalArgument = OmittedOptionalArgumentValue,
    end: Int,
): Blob = // ...
```

This gives the overloads different type signatures. `blob.slice(100)` selects `start`, while
`blob.slice(end = 100)` skips the defaulted marker and selects `end`
Also ensures that the previously invalid positional call `blob.slice(1, "text/plain")` does not become valid.