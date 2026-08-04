# JVM target feasibility prototype

Two designs are tested:

1. `org.w3c.dom.*` expected/actual stubs compile for Kotlin/JS, Kotlin/Wasm, and Kotlin/JVM, but their JVM classes cannot be loaded on Java because the `java.xml` module owns `org.w3c.dom` and `org.w3c.dom.events`. `JvmCollisionTest` records that failure as a passing negative test.
2. The same marker-oriented API works in the JVM-safe `kotlinx.browser.dom.*` namespace. It contains 52 DOM declarations in the Compose HTML dependency closure, an `EventTarget` base, and nine additional event declarations. JVM event registration is deliberately a no-op.

Run the prototype with:

```shell
./gradlew :jvm-target-prototype:compileKotlinJs :jvm-target-prototype:compileKotlinWasmJs :jvm-target-prototype:jvmTest
```
