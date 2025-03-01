plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.alcanl.android.app.sudoku"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alcanl.android.app.sudoku"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    dataBinding.enable = true
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "35.0.0"

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    val roomVersion = "2.6.1"
    implementation("com.github.homayoonahmadi:GroupBoxLayout:1.2.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("com.github.MasayukiSuda:BubbleLayout:v1.2.2")
    implementation("com.github.chnouman:AwesomeDialog:1.0.5")
    implementation("com.github.razaghimahdi:Android-Loading-Dots:1.3.2")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.androidplot:androidplot-core:1.5.10")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation ("com.facebook.android:facebook-login:latest.release")
    implementation("androidx.activity:activity-ktx:1.10.1")
    ksp("androidx.room:room-compiler:$roomVersion")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.compose.material:material:1.7.8")
}

kapt {
    correctErrorTypes = true
}