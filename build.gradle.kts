buildscript {
    val kotlin_version: String by extra("1.8.22")
    val hilt_version: String by extra("2.46.1")
    val google_services_version: String by extra("4.3.15")

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.google.gms:google-services:$google_services_version")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hilt_version")
    }
}

plugins {
    // Note: In Kotlin DSL, plugin version specifications using variables is not supported here.
    // You would typically define plugin versions in the settings.gradle.kts for centralized version management
    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
