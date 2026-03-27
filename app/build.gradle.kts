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
    id("therouter")
}

android {
    namespace = "com.github.xs93.wan.app"
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
            val name = "WanAndroid_${buildType.name}_${defaultConfig.versionName}_$time}.apk"
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
}

hilt {
    enableAggregatingTask = true
    enableExperimentalClasspathAggregation = true
}

dependencies {
    implementation(project(":feature:module_main"))
    implementation(project(":feature:module_music"))
    implementation(project(":feature:module_home"))
    implementation(project(":feature:module_login"))
    implementation(project(":feature:module_navigator"))
    implementation(project(":feature:module_classify"))
    implementation(project(":feature:module_mine"))
    implementation(project(":feature:module_web"))
    implementation(project(":feature:lib_common"))

    implementation(libs.androidx.hilt)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.therouter.router)
    ksp(libs.therouter.ksp)

    debugImplementation(libs.leakcanary)
}