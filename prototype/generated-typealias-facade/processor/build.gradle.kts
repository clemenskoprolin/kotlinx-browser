plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.2.20-Beta2-2.0.2")
    implementation("com.squareup:kotlinpoet-jvm:2.2.0")
}
