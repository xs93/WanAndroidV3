// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version (Versions.agp) apply false
    id("com.android.library") version (Versions.agp) apply false
    id("org.jetbrains.kotlin.android") version (Versions.kotlin) apply false
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}