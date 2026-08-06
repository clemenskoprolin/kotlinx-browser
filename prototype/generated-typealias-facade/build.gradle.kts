@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import org.gradle.api.tasks.Sync

plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

val generatedPortableDom = layout.buildDirectory.dir("generated/portableDom")

val generatePortableDomFacade by tasks.registering(Sync::class) {
    group = "generation"
    description = "Materializes the portable DOM sources emitted by KSP and KotlinPoet."
    dependsOn(":kspKotlinJs")

    from(rootProject.layout.buildDirectory.dir("generated/ksp/js/jsMain/resources")) {
        include("portableDom/**")
    }
    into(generatedPortableDom)
    eachFile {
        path = path.removePrefix("portableDom/")
        if (path.endsWith(".kt.txt")) path = path.removeSuffix(".txt")
    }
    includeEmptyDirs = false
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvm()
    js {
        nodejs()
        compilerOptions {
            optIn.add("kotlin.js.ExperimentalWasmJsInterop")
        }
    }
    wasmJs {
        nodejs()
        compilerOptions {
            optIn.add("kotlin.js.ExperimentalWasmJsInterop")
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(generatedPortableDom.map { it.dir("commonMain/kotlin") })
        }
        // The typealiases are the same for both web targets; only `JsAny` differs per target.
        val webMain by getting {
            kotlin.srcDir(generatedPortableDom.map { it.dir("webMain/kotlin") })
            dependencies {
                api(project(":"))
            }
        }
        val jsMain by getting {
            kotlin.srcDir(generatedPortableDom.map { it.dir("jsMain/kotlin") })
        }
        val wasmJsMain by getting {
            kotlin.srcDir(generatedPortableDom.map { it.dir("wasmJsMain/kotlin") })
        }
        val jvmMain by getting {
            kotlin.srcDir(generatedPortableDom.map { it.dir("jvmMain/kotlin") })
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.matching { task -> task.name.startsWith("compile") && "Kotlin" in task.name }.configureEach {
    dependsOn(generatePortableDomFacade)
}
