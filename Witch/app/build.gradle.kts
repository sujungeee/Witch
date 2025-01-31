plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.ssafy.witch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ssafy.witch"
        minSdk = 26
        targetSdk = 34
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
    viewBinding {
        enable= true
    }
    dataBinding {
        enable= true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewbinding)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // https://github.com/ybq/Android-SpinKit
    implementation ("com.github.ybq:Android-SpinKit:1.4.0")

    // https://github.com/square/retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // https://github.com/square/okhttp
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")

    // https://github.com/square/retrofit/tree/master/retrofit-converters/gson
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("com.prolificinteractive:material-calendarview:1.4.3")

    // 구글 map api(사용)
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.android.gms:play-services-location:20.0.0")

    // 움직이는 애니메이션 - 로티
    implementation ("com.airbnb.android:lottie:5.0.3")

    // security-crypto 라이브러리 추가
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    // lifecycle.viewModelScope 사용 위한 라이브러리 추가
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    // Glide 사용
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
}