// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.8.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.20-RC" apply false
    id("com.google.dagger.hilt.android") version "2.55" apply false
    id("com.android.library") version "8.8.2" apply false
    id("com.google.devtools.ksp") version "2.1.20-RC-1.0.31" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20-RC"
    id("androidx.navigation.safeargs.kotlin") version "2.8.8" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}