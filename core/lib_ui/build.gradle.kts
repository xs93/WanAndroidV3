plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.github.xs93.ui"
    compileSdk = libs.versions.targetSdk.get().toInt()

    defaultConfig {
        minSdk = 23
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core:lib_core"))
    implementation(project(":core:lib_kv"))

    api(libs.androidx.core.kts)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.activity.ktx)
    api(libs.androidx.fragment.ktx)

    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.bundles.kotlinx.coroutines)

    api(libs.androidx.viewpager2)

    api(libs.logger)
}