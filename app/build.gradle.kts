plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.hilt)
}

android {
    namespace = "com.example.scrambleword"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.scrambleword"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    buildFeatures {

        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime)
    implementation(libs.material)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.livedata)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.android.hilt)
    implementation(libs.android.hilt.navigation)
    ksp(libs.android.hilt.compiler)

    implementation(libs.compose.preview)
    debugImplementation(libs.compose.tooling)
    implementation(libs.compose.bom)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.material.icons)
    implementation(libs.material.icons.ex)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mock)

    implementation(libs.retrofit.gson)
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging)


    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}