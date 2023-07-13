@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.xs93.framework"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro", "proguard-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    api(libs.androidx.core.kts)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.fragment.ktx)


    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    ksp(libs.androidx.lifecycle.compiler)

    api(libs.bundles.kotlinx.coroutines)

    api(libs.androidx.viewpager2)

    api(libs.mmkv)
    api(libs.bundles.retrofit2)
    ksp(libs.moshi.kotlin.codegen)

    api(libs.coil)

    api(libs.logger)
}