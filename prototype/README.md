# JVM DOM feasibility prototypes

These experiments test how a portable Compose HTML DOM API could reuse kotlinx-browser on JS/Wasm
and provide type-only JVM stubs.

| Approach | Common API | Platform mapping | Result                                |
| --- | --- | --- |---------------------------------------|
| Direct JVM target | `org.w3c.dom.*` | JVM classes also in `org.w3c.dom.*` | Compiles, but fails to load on JDK 21 |
| Typealias facade | `kotlinx.browser.dom.*` | JS/Wasm aliases to `org.w3c.dom.*`; safe JVM classes | Works for 53 selected classes         |

## 1. Direct JVM target

The [`jvm-target`](jvm-target/) module generates ordinary JVM actuals with the same names as the
common declarations:

```text
expect org.w3c.dom.HTMLDivElement
    -> actual org.w3c.dom.HTMLDivElement
```

The class is present in the JAR, but `Class.forName` cannot load it because `java.xml` owns the
package. [`JvmCollisionTest`](jvm-target/src/jvmTest/kotlin/JvmCollisionTest.kt) preserves this as an
expected failure. The module also contains a safe-package control experiment.

## 2. Typealias facade

The working [`typealias-facade`](typealias-facade/) module moves the common API itself:

```text
kotlinx.browser.dom.HTMLDivElement
    -> JS/Wasm actual typealias -> org.w3c.dom.HTMLDivElement
    -> JVM actual class         -> kotlinx.browser.dom.HTMLDivElement
```

All 53 classifiers compile for the three targets. JS/Wasm aliases reuse the real
kotlinx-browser types, and every JVM marker class loads successfully. DOM members remain out of
scope.

## Verification

```shell
./gradlew :jvm-target-prototype:jvmTest --offline
./gradlew :typealias-facade-prototype:compileTestKotlinJs :typealias-facade-prototype:compileTestKotlinWasmJs :typealias-facade-prototype:jvmTest --offline
```
