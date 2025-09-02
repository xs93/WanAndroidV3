@file:Suppress("UnstableApiUsage")
pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://www.jitpack.io")
        google()
        mavenCentral()
    }
}

rootProject.name = "WanAndroidV3"
include(":app")
include(":app-demo")
include(":core:lib_common")
include(":core:lib_framework")
include(":core:lib_coil")
include(":core:lib_network")
include(":core:lib_utils")
include(":core:lib_gdview")
include(":core:lib_camera")
include(":core:lib_kv")

include(":feature:feat_music")