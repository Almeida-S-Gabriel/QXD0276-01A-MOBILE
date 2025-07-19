plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.compose")
}

android {
    namespace = "com.example.authapp2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.authapp2"
        minSdk = 34
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    // Firebase BOM - Garante que todas as bibliotecas Firebase são compatíveis
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx") // Versão removida
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Jetpack Compose - BOM atualizado
    implementation(platform("androidx.compose:compose-bom:2024.05.00")) // <-- ATUALIZADO
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3") // <-- Apenas esta
    implementation("androidx.compose.ui:ui-tooling-preview")
    // implementation(libs.androidx.material3.android) // <-- LINHA APAGADA

    debugImplementation("androidx.compose.ui:ui-tooling")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7") // Versão um pouco mais nova

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.1") // Versões mais novas
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.1")

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00")) // <-- ATUALIZADO
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}