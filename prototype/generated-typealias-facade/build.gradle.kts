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
        val jsMain by getting {
            kotlin.srcDir(generatedPortableDom.map { it.dir("jsMain/kotlin") })
            dependencies {
                api(project(":"))
            }
        }
        val wasmJsMain by getting {
            kotlin.srcDir(generatedPortableDom.map { it.dir("wasmJsMain/kotlin") })
            dependencies {
                api(project(":"))
            }
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
