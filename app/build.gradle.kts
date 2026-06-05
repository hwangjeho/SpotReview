import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
}

// .env 파일 로드
val envProperties = Properties().apply {
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        load(FileInputStream(envFile))
    }
}

android {
    namespace = "com.application.spotreview"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.application.spotreview"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // BuildConfig 변수 생성
        buildConfigField("String", "NAVER_SEARCH_ID", "\"${envProperties.getProperty("NAVER_SEARCH_ID")}\"")
        buildConfigField("String", "NAVER_SEARCH_SECRET", "\"${envProperties.getProperty("NAVER_SEARCH_SECRET")}\"")
        buildConfigField("String", "NCP_CLIENT_ID", "\"${envProperties.getProperty("NCP_CLIENT_ID")}\"")
        
        // Manifest Placeholder 설정
        manifestPlaceholders["ncpClientId"] = envProperties.getProperty("NCP_CLIENT_ID") ?: ""
    }

    buildFeatures {
        buildConfig = true
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
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.naver.maps:map-sdk:3.23.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
