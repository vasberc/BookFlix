plugins {
    id("com.android.library")
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.vasberc.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

//Commented out because the domain module does not have enabled the compile check feature
//and is not possible to check at compile time dependencies from domain module
//ksp {
//    arg("KOIN_CONFIG_CHECK", "true")
//}

dependencies {
    implementation(project(":domain"))
    implementation(platform(libs.androidx.compose.bom))
    ksp(libs.koinKsp)
    implementation(libs.bundles.core)
    implementation(libs.bundles.presentation)
    coreLibraryDesugaring(libs.desugaring)
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.androidTesting)
    debugImplementation(libs.bundles.presentationDebug)
}