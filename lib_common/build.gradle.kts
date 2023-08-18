@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}
android {
    namespace = "com.github.xs93.common"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro", "proguard-rules.pro")
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

kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = true
    enableExperimentalClasspathAggregation = true
}

dependencies {

    api(project(mapOf("path" to ":lib_utils")))
    api(project(mapOf("path" to ":lib_network")))
    api(project(mapOf("path" to ":lib_framework")))

    api(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    api(libs.brvah4)
    api(libs.androidx.paging3)

    implementation(libs.androidx.hilt)
    kapt(libs.androidx.hilt.compiler)
}