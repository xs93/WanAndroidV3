plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.name)
    }
}

android {
    namespace = "com.github.xs93.wanandroid.web"

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
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.material)
    implementation(ThirdPart.agentWeb_core)
    implementation(ThirdPart.agentWeb_file_chooser)
    implementation(ThirdPart.agentWeb_downloader)

    kapt(ThirdPart.arouter_compiler)

    implementation(project(mapOf("path" to ":library-common")))

    testImplementation(Depend.junit)
    androidTestImplementation(Depend.junitExt)
    androidTestImplementation(Depend.espressoCore)
}