plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.alcanl.android.app.sudoku"
    compileSdk = 35

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    dataBinding.enable = true
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "35.0.0"
}

dependencies {

    val roomVersion = "2.6.1"
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.github.cdflynn:checkview:v1.2")
    implementation("com.github.homayoonahmadi:GroupBoxLayout:1.2.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("com.github.MasayukiSuda:BubbleLayout:v1.2.2")
    implementation("com.github.chnouman:AwesomeDialog:1.0.5")
    implementation("com.github.razaghimahdi:Android-Loading-Dots:1.3.2")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.androidplot:androidplot-core:1.5.11")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.8")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.8")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.55")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation ("com.facebook.android:facebook-login:18.0.2")
    implementation("androidx.activity:activity-ktx:1.10.1")
    ksp("androidx.room:room-compiler:$roomVersion")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    implementation("com.google.dagger:hilt-android:2.55")
    ksp("com.google.dagger:hilt-compiler:2.55")
    ksp("com.google.dagger:dagger-compiler:2.55")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.compose.material:material:1.7.8")
}
