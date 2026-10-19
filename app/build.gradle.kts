plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Kích hoạt Google services
}

android {
    namespace = "vn.edu.ueh.baoho.ssharing"
    compileSdk = 36

    defaultConfig {
        applicationId = "vn.edu.ueh.baoho.ssharing"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.ktx)
    implementation(libs.constraintlayout)
    // Thư viện Google ML Kit Text Recognition (Dùng cho quét OCR mã voucher)
    implementation("com.google.mlkit:text-recognition:16.0.1")

    // ==========================================
    // 1. Thư viện Room Database (SQLite Cục bộ)
    // ==========================================
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // ==========================================
    // 2. Thư viện Firebase (BOM & Dịch vụ)
    // ==========================================
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-firestore") // Cloud Firestore
    implementation("com.google.firebase:firebase-auth")      // Xác thực người dùng
    implementation("com.google.firebase:firebase-storage")   // Lưu trữ hình ảnh

    // ==========================================
    // 3. Thư viện Google Maps & Định vị (GPS)
    // ==========================================
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}