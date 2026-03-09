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
    namespace = "com.gamechanger.assessment.coreui"
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

    // Compose
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.compose)
    debugApi(libs.bundles.compose.debug)

    // Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)

    // Coil
    api(libs.bundles.coil)

    // Icons-library
    api(libs.simple.icons)

    // OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)

    // ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // UI-Testing
    androidTestImplementation(libs.androidx.test.runner)
}