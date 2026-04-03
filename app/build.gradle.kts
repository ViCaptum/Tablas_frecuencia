plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.curso.tablas_frecuencia"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.curso.tablas_frecuencia"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.3.0"

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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation ("androidx.camera:camera-camera2:1.3.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-view:1.3.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}



androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                output.outputFileName.set("Tablas_Frecuencia_${android.defaultConfig.versionName}.apk")
            }
        }
    }
}