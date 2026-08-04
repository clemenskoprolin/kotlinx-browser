pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        kotlin("multiplatform").version("2.2.20-Beta2")
        kotlin("jvm").version("2.2.20-Beta2")
        id("com.google.devtools.ksp").version("2.2.20-Beta2-2.0.2")
    }
}

include(":generator")
include(":jvm-target-prototype")
include(":typealias-facade-prototype")
include(":portable-dom-ksp-processor")
include(":generated-typealias-facade-prototype")

project(":jvm-target-prototype").projectDir = file("prototype/jvm-target")
project(":typealias-facade-prototype").projectDir = file("prototype/typealias-facade")
project(":portable-dom-ksp-processor").projectDir = file("prototype/generated-typealias-facade/processor")
project(":generated-typealias-facade-prototype").projectDir = file("prototype/generated-typealias-facade")

rootProject.name = "kotlinx-browser"
