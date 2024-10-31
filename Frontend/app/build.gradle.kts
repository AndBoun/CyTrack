plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.CyTrack"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.CyTrack"
        minSdk = 30
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}


androidComponents {
    onVariants { variant ->
        tasks.register<Javadoc>("javadoc${variant.name.capitalize()}") {

            doFirst {
                configurations["implementation"]
                    .filter { it.name.endsWith(".aar") }
                    .forEach { aar ->
                        copy {
                            from(zipTree(aar))
                            include("**/classes.jar")
                            into("${buildDir}/tmp/aarsToJars/${aar.name.replace(".aar", "")}")
                        }
                    }
            }

            configurations["implementation"].attributes {
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
            }
            configurations["implementation"].isCanBeResolved = true

            // Set the source directories for generating Javadoc
            source = fileTree(mapOf("dir" to "src/main/java", "includes" to listOf("**/*.java")))

            // Include Android's boot classpath and implementation dependencies in the Javadoc classpath
            classpath = files(android.bootClasspath.joinToString(File.pathSeparator)) +
                    configurations["implementation"] +
                    fileTree("${buildDir}/tmp/aarsToJars/")

            if (variant.buildType == "release") {
                classpath += variant.compileConfiguration
            }

            setDestinationDir(file("${buildDir}/outputs/javadoc/"))

            // Set the visibility level for the Javadoc members
            (options as StandardJavadocDocletOptions).apply {
                memberLevel = JavadocMemberLevel.PRIVATE
            }

            // Disable failing the build on errors in Javadoc generation
            isFailOnError = false

            // Exclude auto-generated Android classes from the Javadoc
            exclude("**/BuildConfig.java", "**/R.java")
        }
    }
}


dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.splashscreen)
    implementation(libs.legacy.support.v4)
    implementation(libs.volley)
    implementation(libs.storage)
    implementation(libs.foundation.layout.android)
    implementation(libs.material3.android)
    implementation(libs.ui.tooling)
    implementation(libs.core.ktx)
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.accompanist.themeadapter.material3)
    debugImplementation(libs.androidx.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.4")
    implementation("org.java-websocket:Java-WebSocket:1.5.2")
}

