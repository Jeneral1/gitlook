import java.util.Properties

plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("org.jetbrains.dokka")
}

android {
    namespace = "com.efs.git_look"
    compileSdk = 33

    defaultConfig {
        applicationId ="com.efs.git_look"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "API_TOKEN", "\"${properties.getProperty("API_TOKEN")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_16
        targetCompatibility = JavaVersion.VERSION_16
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_16.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_version"] as String
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-common:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation ("androidx.activity:activity-compose:1.6.1")
    implementation ("androidx.compose.ui:ui:${rootProject.extra["compose_version"]}")
    implementation ("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["compose_version"]}")
    implementation("androidx.compose.material:material:1.3.1")
    implementation ("androidx.compose.material3:material3:1.1.0-alpha03")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:${rootProject.extra["compose_version"]}")
    debugImplementation ("androidx.compose.ui:ui-tooling:${rootProject.extra["compose_version"]}")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:${rootProject.extra["compose_version"]}")


    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    //Paging
    implementation("androidx.paging:paging-compose:1.0.0-alpha17")

    //Library to Load Image from URL
    implementation ("io.coil-kt:coil-compose:2.1.0")
}