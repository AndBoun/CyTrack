import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.Platform
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.dokka") version "1.9.20"
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.20")
    }
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
        debug {
            isTestCoverageEnabled = true
//            enableAndroidTestCoverage = true
//            enableUnitTestCoverage = true
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
                attribute(
                    Usage.USAGE_ATTRIBUTE,
                    objects.named(Usage::class.java, Usage.JAVA_RUNTIME)
                )
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

tasks.withType<DokkaTask>().configureEach {

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
//        customAssets = listOf(file("my-image.png"))
//        customStyleSheets = listOf(file("my-styles.css"))
        footerMessage = "(c) 2024 CyTrack"
        separateInheritedMembers = false
//        templatesDir = file("dokka/templates")
        mergeImplicitExpectActualDeclarations = false
    }

    outputDirectory.set(layout.buildDirectory.dir("documentation/html"))
    dokkaSourceSets {

        named("main") {
            sourceRoots.from(file("src/main/java"))
            sourceRoots.from(file("src/main/kotlin"))
            perPackageOption {
                matchingRegex.set("com.example.compose.*")
                suppress.set(true)
            }
        }
        configureEach {
            // Exclude inherited members
            suppressInheritedMembers.set(true)

            // Exclude specific packages, classes, or files if needed
            // For example, exclude Android framework packages:
            perPackageOption {
                matchingRegex.set("android.*") // or "androidx.*" to exclude AndroidX classes too
                suppress.set(true)
            }

            skipEmptyPackages.set(true)
            documentedVisibilities.set(
                setOf(
                    Visibility.PUBLIC,
                    Visibility.PROTECTED,
                    Visibility.INTERNAL,
                    Visibility.PRIVATE,
                    Visibility.PACKAGE
                )
            )
        }
    }
}

dependencies {
    implementation(libs.activity)
    implementation(libs.core.splashscreen)
    implementation(libs.legacy.support.v4)
    implementation(libs.storage)


    // Java UI and Activities
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)


    // Java Networking Dependencies
    implementation(libs.volley)
    implementation("org.java-websocket:Java-WebSocket:1.5.2")


    // Ktor Dependencies for multiplatform
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)


    // Coil Dependencies for KMP Image Loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor3)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    androidTestImplementation(platform(libs.compose.bom))


    // Dokka Plugin for Java
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.9.20")


    // Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")

    // Compose Testing
    // Test rules and transitive dependencies:
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.5")
    // Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>:
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")


    // Compose and UI Dependencies (for declarative UI)
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.accompanist.themeadapter.material3)
    implementation(libs.material3.android)
    implementation(libs.foundation.layout.android)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.core.ktx)
    implementation(libs.ui.tooling)
    implementation(libs.androidx.ui)

    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.4")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:$1.3.1")

    // Vico Graph Dependencies
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    // Graph
    implementation ("com.jjoe64:graphview:4.2.2")
}

