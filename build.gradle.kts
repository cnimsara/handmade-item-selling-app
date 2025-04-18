// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}
// Project-level build.gradle
buildscript {
    val kotlinVersion: String by extra("2.1.0") // Define Kotlin version here

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:7.3.1") // Ensure you're using the latest stable Android plugin
        classpath( "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath ("com.google.gms:google-services:4.3.15" )// Google Services plugin if needed

    }
    }



