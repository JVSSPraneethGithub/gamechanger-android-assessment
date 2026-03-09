import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(JavaVersion.VERSION_21.toString())
    }
}

android {
    namespace = "com.gamechanger.assessment.network"
    compileSdk {
        version = libs.versions.android.targetSdk.get().toDoubleOrNull()?.let { targetSdk ->
            release(targetSdk.toInt()) {
                minorApiLevel = targetSdk.toString().substringAfter(".").toIntOrNull()
            }
        }
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toDoubleOrNull()?.toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)

    // Network
    implementation(platform(libs.retrofit.bom))
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.network)

    // UI-Testing
    androidTestImplementation(libs.androidx.test.runner)
}