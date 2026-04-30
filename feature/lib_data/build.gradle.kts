plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "com.github.xs93.wan.data"
    compileSdk {
        version = release(libs.versions.targetSdk.get().toInt())
    }

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
}

dependencies {

    implementation(project(":core:lib_network"))
    implementation(project(":core:lib_core"))
    implementation(project(":core:lib_kv"))
    implementation(project(":feature:lib_model"))

    implementation(libs.mmkv)

    implementation(libs.androidx.hilt)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}