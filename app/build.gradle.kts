plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
}

android {

    namespace = "com.github.xs93.wanandroid"

    compileSdk = BuildConfig.targetSdk

    defaultConfig {
        applicationId = BuildConfig.applicationId
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(mapOf("path" to ":library-common")))
    implementation(project(mapOf("path" to ":business-main")))
    implementation(project(mapOf("path" to ":business-home")))
    implementation(project(mapOf("path" to ":business-login")))
    implementation(project(mapOf("path" to ":business-web")))
}