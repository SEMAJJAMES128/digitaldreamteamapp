// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.0") // Make sure this version matches your Android Gradle Plugin version
        classpath("com.google.gms:google-services:4.3.14") // Make sure this version is correct
        // Add other classpath dependencies for plugins here
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // Include other repositories here if needed
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
