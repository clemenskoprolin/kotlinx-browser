# Portable DOM typealias facade prototype

This module tests the proposed facade architecture with the complete marker allowlist derived from
Compose HTML: some DOM classifiers plus the `EventTarget` dependency. `NodeList` and `ValidityState`
are included as the first types pulled in by ordinary member signatures; `GetRootNodeOptions` and
`ScrollToOptions` support members with defaulted option arguments. Its core inheritance paths are:

```text
EventTarget <- Node <- Element <- HTMLElement <- HTMLDivElement
```

- `commonMain` declares portable `expect` classes in the JVM-safe `kotlinx.browser.dom` package.
- `jsMain` and `wasmJsMain` depend on the root kotlinx-browser project and use `actual typealias`
  declarations to its existing `org.w3c.dom` types.
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
