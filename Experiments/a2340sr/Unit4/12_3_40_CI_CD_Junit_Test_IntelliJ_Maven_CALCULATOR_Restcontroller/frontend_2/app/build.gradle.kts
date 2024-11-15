plugins {
    id("com.android.application")
}

android {
    namespace = "com.coms309.calculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.coms309.calculator"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    // JUnit for unit testing
    testImplementation("junit:junit:4.13.2")

    testImplementation("org.mockito:mockito-core:4.6.1")

    // AndroidX Test - Core libraries for instrumentation tests
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Robolectric for running Android unit tests on the JVM
    testImplementation("org.robolectric:robolectric:4.9")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    // http requests
    implementation("com.android.volley:volley:1.2.1")

}