@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        // Renders the internal diagnostic names that the `@file:Suppress` lists refer to.
        freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
    }

    jvm()
    js {
        nodejs()
    }
    wasmJs {
        nodejs()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        val webMain by getting {
            dependencies {
                api(project(":"))
            }
        }
    }
}
