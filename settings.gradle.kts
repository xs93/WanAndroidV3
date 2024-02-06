@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
    }
}

rootProject.name = "WanAndroidV3"
include(":app")
include(":app-compose")
include(":mod_web")
include(":lib_common")
include(":lib_framework")
include(":lib_framework_compose")
include(":lib_coil")
include(":lib_network")
include(":lib_utils")
