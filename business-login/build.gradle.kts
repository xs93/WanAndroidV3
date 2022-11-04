plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {

    namespace = "com.github.xs93.wanandroid.login"

    compileSdk = BuildConfig.targetSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
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
        viewBinding = true
    }
}

dependencies {

    implementation(project(mapOf("path" to ":library-common")))

    kapt(ThirdPart.arouter_compiler)
    kapt(ThirdPart.moshi_kotlin_codegen)

    testImplementation(Depend.junit)
    androidTestImplementation(Depend.espressoCore)
    androidTestImplementation(Depend.junitExt)
}