@file:Suppress("UnstableApiUsage")


rootProject.name = "WanAndroidV3"
include(":app")
include(":app-demo")

include(":core:lib_core")
include(":core:lib_network")
include(":core:lib_kv")
include(":core:lib_ui")
include(":core:lib_coil")
include(":core:lib_widget")
include(":core:lib_camera")

include(":feature:lib_data")
include(":feature:lib_domain")
include(":feature:lib_router")
include(":feature:lib_common")
include(":feature:module_main")
include(":feature:module_music")
include(":feature:module_home")
include(":feature:module_login")

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