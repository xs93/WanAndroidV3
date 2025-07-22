@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.google.hilt)
}

android {
    namespace = "com.github.xs93.wanandroid.app"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.xs93.wanandroid"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    applicationVariants.all { ->
        outputs.all {
            val dataFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            dataFormat.timeZone = TimeZone.getTimeZone("GMT+08")
            val time = dataFormat.format(Date())
            val name = "Poppy_${buildType.name}_${defaultConfig.versionName}_$time}.apk"
            (this as BaseVariantOutputImpl).outputFileName = name
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":lib_common"))

    ksp(libs.androidx.lifecycle.compiler)

    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.banner)
    implementation(libs.viewPagerIndicator)

    implementation(libs.flexbox)

    implementation(libs.androidx.hilt)
    ksp(libs.androidx.hilt.compiler)

    debugImplementation(libs.leakcanary)
}