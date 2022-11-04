plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {

    namespace = "com.github.xs93.wanandroid.common"

    compileSdk = BuildConfig.targetSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

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
    api(AndroidX.coreKtx)
    api(AndroidX.appcompat)
    api(AndroidX.material)
    api(AndroidX.constraintlayout)

    api(AndroidX.lifecycle_runtime_ktx)
    api(AndroidX.lifecycle_liveData_Ktx)
    api(AndroidX.lifecycle_viewmodel_ktx)

    api(Kotlin.kotlinx_coroutines_core)
    api(Kotlin.kotlinx_coroutines_android)

    api(ThirdPart.arouter_api)

    api(ThirdPart.logger)
    api(ThirdPart.androidBase)
    api(ThirdPart.easyRetrofit)
    api(ThirdPart.mmkvKtx)
    api(ThirdPart.checkableView)

    kapt(ThirdPart.moshi_kotlin_codegen)

    api(ThirdPart.coil)
    api(ThirdPart.BRVAH)

    api(ThirdPart.smart_refresh_layout_kernel)
    api(ThirdPart.smart_refresh_header_material)
    api(ThirdPart.smart_refresh_footer_ball)
}