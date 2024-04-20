plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "edu.gvsu.cis.camerafuntime"
    compileSdk = 34

    buildFeatures{
        viewBinding = true
    }

    defaultConfig {
        applicationId = "edu.gvsu.cis.camerafuntime"

        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.camera:camera-core:1.2.2")
    implementation("androidx.camera:camera-camera2:1.2.2")
    implementation("androidx.camera:camera-lifecycle:1.2.2")
    implementation("androidx.camera:camera-video:1.2.2")

    implementation("androidx.camera:camera-view:1.2.2")
    implementation("androidx.camera:camera-extensions:1.2.2")

    implementation("com.github.bumptech.glide:glide:4.13.2")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.media3:media3-common:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}