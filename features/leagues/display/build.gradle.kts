import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
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
    namespace = "com.gamechanger.assessment.features.leagues"
    compileSdk {
        version = libs.versions.android.targetSdk.get().toDoubleOrNull()?.let { targetSdk ->
            release(targetSdk.toInt()) {
                minorApiLevel = targetSdk.toString().substringAfter(".").toIntOrNull()
            }
        }
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toDoubleOrNull()?.toInt()

        testInstrumentationRunner =
            "com.gamechanger.assessment.testing.runner.GameChangerAssessmentTestRunner"
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

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)

    // Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.bundles.hilt)

    // Modules
    api(project(":coreUi"))
    implementation(project(":framework-leagues"))

    // Unit-Testing
    testImplementation(libs.bundles.unit.testing)

    // UI-Testing
    androidTestImplementation(project(":testing"))
}