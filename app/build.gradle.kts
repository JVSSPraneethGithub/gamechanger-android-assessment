import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(JavaVersion.VERSION_21.toString())
    }
}

android {
    namespace = "com.gamechanger.assessment"
    compileSdk {
        version = libs.versions.android.targetSdk.get().toDoubleOrNull()?.let { targetSdk ->
            release(targetSdk.toInt()) {
                minorApiLevel = targetSdk.toString().substringAfter(".").toIntOrNull()
            }
        }
    }

    defaultConfig {
        applicationId = "com.gamechanger.assessment"
        minSdk = libs.versions.android.minSdk.get().toDoubleOrNull()?.toInt()
        targetSdk = libs.versions.android.targetSdk.get().toDoubleOrNull()?.toInt()

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "com.gamechanger.assessment.testing.runner.GameChangerAssessmentTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Androidx
    implementation(libs.bundles.androidx)

    // Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.bundles.hilt)

    // Modules
    implementation(project(":feature-leagues"))
    implementation(project(":feature-teams"))

    // Unit-tests
    testImplementation(libs.bundles.unit.testing)

    // UI-tests
    androidTestImplementation(project(":testing"))
}