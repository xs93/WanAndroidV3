plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.github.xs93.wan.music"
    compileSdk = libs.versions.targetSdk.get().toInt()

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
}

dependencies {
    implementation(project(":feature:lib_common"))

    implementation(libs.therouter.router)
    ksp(libs.therouter.ksp)

    implementation(libs.androidx.meida3.exoplayer)
    implementation(libs.androidx.meida3.ui)
    implementation(libs.androidx.meida3.common)
    implementation(libs.androidx.meida3.commonKtx)
    implementation(libs.androidx.meida3.session)
    implementation(libs.androidx.meida3.datasource.okhttp)
}