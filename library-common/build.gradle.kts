plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = BuildConfig.targetSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    api(AndroidX.coreKtx)
    api(AndroidX.appcompat)
    api(AndroidX.material)
    api(AndroidX.constraintlayout)
    api(AndroidX.Lifecycle.viewModelKtx)
    api(ThirdPart.androidBase)
    api(ThirdPart.easyRetrofit)
    api(ThirdPart.mmkvKtx)
    api(ThirdPart.checkableView)
}