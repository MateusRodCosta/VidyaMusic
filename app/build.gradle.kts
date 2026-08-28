import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.aboutLibraries.android)
    alias(libs.plugins.baselineprofile)
}

val keystorePropertiesFile = rootProject.file("key.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(keystorePropertiesFile.inputStream())
}

android {
    namespace = "com.mateusrodcosta.apps.vidyamusic"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    // Main portion of the version code, usually implies a versionName change
    val baseVersionCode = 23
    // Minor portion code of the version code, usually reserved for emergencies
    // Should only use values from 0 up to 9
    val minorVersionCode = 0
    // XXYZ, where XX is the baseVersionCode, Y is the minorVersionCode,
    // and Z is reserved for ABIs (which is unused, so it's always zero)
    val versionCodeVar = baseVersionCode * 100 + minorVersionCode * 10

    val baseVersionName = "3.0.2"
    val versionNameVar = "$baseVersionName-R"

    defaultConfig {
        applicationId = "com.mateusrodcosta.apps.vidyamusic"
        minSdk = 28
        //noinspection OldTargetApi
        targetSdk = 36
        versionCode = versionCodeVar
        versionName = versionNameVar

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            // Enable v3 signing only, also on debug builds
            enableV1Signing = false
            enableV2Signing = false
            enableV3Signing = true
        }

        if (keystorePropertiesFile.exists()) {
            create("release") {
                if (keystorePropertiesFile.exists()) {
                    keyAlias = keystoreProperties["keyAlias"] as String
                    keyPassword = keystoreProperties["keyPassword"] as String
                    storeFile = file(keystoreProperties["storeFile"] as String)
                    storePassword = keystoreProperties["storePassword"] as String
                }

                // Enable v3 signing only, which will be used on modern Android (9+)
                enableV1Signing = false
                enableV2Signing = false
                enableV3Signing = true
            }
        }
    }

    buildTypes {
        release {
            optimization {
                enable = true
            }

            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = true
    }
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        // For https://gitlab.com/fdroid/fdroidserver/-/work_items/1056
        // Dependency Metadata Block is kept for Google Play Console's AABs
        includeInApk = false
    }
}

kotlin {
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_4
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":app:baselineprofile"))

    implementation(libs.kotlinx.coroutines.core)
    runtimeOnly(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.window.core)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.core.viewmodel)
    implementation(libs.koin.annotations)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestRuntimeOnly(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugRuntimeOnly(libs.androidx.compose.ui.test.manifest)

    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":features:shared"))
    implementation(project(":features:playlist"))
    implementation(project(":features:settings"))
}