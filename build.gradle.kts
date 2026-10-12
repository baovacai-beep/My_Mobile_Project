plugins {
    alias(libs.plugins.android.application) apply false
    // Thêm plugin này nếu chưa có:
    id("com.google.gms.google-services") version "4.4.2" apply false
}