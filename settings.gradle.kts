@file:Suppress("UnstableApiUsage")

include(":lib_utils")


include(":lib_network")


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
include(":lib_framework")
include(":lib_common")
