import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.parcelize)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(JavaVersion.VERSION_21.toString())
    }
}

android {
    namespace = "com.gamechanger.assessment.framework.teams"
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
}

dependencies {

    // Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)

    // Modules
    implementation(project(":network"))
    implementation(project(":persistence"))

    // Unit-tests
    testImplementation(libs.bundles.unit.testing)

    // UI-Testing
    androidTestImplementation(libs.androidx.test.runner)
}