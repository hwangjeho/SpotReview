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
    // 1. SDK 33 호환 activity-ktx
    implementation("androidx.activity:activity-ktx:1.7.2")

    // 2. 최신 libs.appcompat을 지우고 SDK 33 호환 버전(1.6.1)으로 고정
    implementation("androidx.appcompat:appcompat:1.6.1")

    // 3. SDK 33 호환 constraintlayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // 4. 머티리얼 라이브러리도 안전하게 문자열로 고정 (기존 1.11.0 유지)
    implementation("com.google.android.material:material:1.11.0")

    // 네이버 지도 SDK
    implementation("com.naver.maps:map-sdk:3.23.2")

    // 인터넷 통신 라이브러리 (Retrofit)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // JSON 데이터를 자바 객체로 파싱해주는 컨버터 (Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
