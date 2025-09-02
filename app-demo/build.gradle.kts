plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.github.xs93.demo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.github.xs93.demo"
        minSdk = 26
        targetSdk = 36

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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    api(project(":core:lib_framework"))
    api(project(":core:lib_coil"))
    api(project(":core:lib_network"))
    api(project(":core:lib_utils"))
    api(project(":core:lib_gdview"))
    api(project(":core:lib_camera"))
    api(project(":core:lib_kv"))

    implementation(libs.mmkv)
    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}