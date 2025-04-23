@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
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
        viewBinding = true
    }
}

hilt {
    enableAggregatingTask = true
    enableExperimentalClasspathAggregation = true
}

dependencies {

    api(project(":lib_framework"))
    api(project(":lib_coil"))
    api(project(":lib_network"))
    api(project(":lib_utils"))
    api(project(":lib_gdview"))
    api(project(":lib_kv"))


    api(libs.brvah4)
    api(libs.smartRefreshLayout.core)
    api(libs.smartRefreshLayout.header.material)
    api(libs.smartRefreshLayout.footer.ball)

    api(libs.multiStateLayout)
    api(libs.checkableView)
    api(libs.avLoadingIndicatorView)

    api(libs.androidx.webkit)

    api(libs.androidAutoSize)

    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.androidx.hilt)
    ksp(libs.androidx.hilt.compiler)

    api(libs.mmkv)
}