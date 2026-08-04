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
    }
}
