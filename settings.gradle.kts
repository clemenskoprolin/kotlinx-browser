pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        kotlin("multiplatform").version("2.2.20-Beta2")
    }
}

include(":generator")
include(":jvm-target-prototype")
include(":typealias-facade-prototype")

project(":jvm-target-prototype").projectDir = file("prototype/jvm-target")
project(":typealias-facade-prototype").projectDir = file("prototype/typealias-facade")

rootProject.name = "kotlinx-browser"
