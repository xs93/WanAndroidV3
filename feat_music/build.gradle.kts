plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.github.xs93.wanandroid.music"
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
    implementation(project(":lib_common"))
    api(libs.androidx.meida3.exoplayer)
    api(libs.androidx.meida3.ui)
    api(libs.androidx.meida3.common)
    api(libs.androidx.meida3.commonKtx)
    api(libs.androidx.meida3.session)
    api(libs.androidx.meida3.datasource.okhttp)
}