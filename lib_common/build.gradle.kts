@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
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
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        viewBinding = true
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
    api(libs.smartRefreshLayout.core)
    api(libs.smartRefreshLayout.header.material)
    api(libs.smartRefreshLayout.footer.ball)

    api(libs.multiStateLayout)
    api(libs.avLoadingIndicatorView)

    implementation(libs.androidx.hilt)
    kapt(libs.androidx.hilt.compiler)
}