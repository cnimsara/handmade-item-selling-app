plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
   // id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.handmadeitem"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.handmadeitem"
        minSdk = 29
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
//    implementation("com.google.firebase:firebase-analytics")
//        implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.21" ) // Make sure the version is correct
//        // Add other Firebase dependencies if needed
    implementation (platform("com.google.firebase:firebase-bom:33.10.0"))  // Firebase BOM for version compatibility
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-common")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")




}