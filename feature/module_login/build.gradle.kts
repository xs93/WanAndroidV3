plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.github.xs93.wan.login"
    compileSdk = libs.versions.targetSdk.get().toInt()
    resourcePrefix = "login_"
    compileSdk {
        version = release(libs.versions.targetSdk.get().toInt())
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.therouter.router)
    ksp(libs.therouter.ksp)

    implementation(libs.androidx.hilt)
    ksp(libs.androidx.hilt.compiler)
    implementation(project(":feature:lib_common"))
}