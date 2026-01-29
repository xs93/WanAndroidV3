@file:Suppress("UnstableApiUsage")

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

include(":feature:module_main")
include(":feature:module_music")
include(":feature:module_home")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://www.jitpack.io")
    }
}