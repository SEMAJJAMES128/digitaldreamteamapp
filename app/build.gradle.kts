fun kapt(s: String) {

}

plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Corrected plugin ID
}

android {
    namespace = "com.example.digitaldreamteamapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.digitaldreamteamapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val compose_bom_version: String by extra("2023.05.01")
val hilt_version: String by extra("2.46.1")
val hilt_navigation_compose_version: String by extra("1.1.0-alpha01")
val firebase_bom_version: String by extra("32.1.1")

dependencies {
    // AndroidX and Material Components
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:22.3.1")

    // Test Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Firebase BOM and Firestore
    implementation(platform("com.google.firebase:firebase-bom:$firebase_bom_version"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Compose
    implementation(platform("androidx.compose:compose-bom:$compose_bom_version"))
    implementation("androidx.compose.material:material")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    // Hilt Navigation Compose
    implementation("androidx.hilt:hilt-navigation-compose:$hilt_navigation_compose_version")
}





