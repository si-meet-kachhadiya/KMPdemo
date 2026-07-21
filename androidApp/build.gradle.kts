import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(projects.sharedLogic)
    implementation("in.co.niteshkukreja:kmp-sdk:1.0.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.kotlinx.coroutines.android)
    implementation("io.coil-kt:coil-compose:2.7.0")

    debugImplementation(libs.androidx.ui.tooling)
}

fun gitCommit(): String = runCatching {
    providers.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
    }.standardOutput.asText.get().trim()
}.getOrDefault("unknown")

fun gitBranch(): String = runCatching {
    providers.exec {
        commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
    }.standardOutput.asText.get().trim()
}.getOrDefault("unknown")

fun buildTimeUtc(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(Date())
}

android {
    namespace = "com.kmpdemo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.kmpdemo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "BUILD_TIME", "\"${buildTimeUtc()}\"")
        buildConfigField("String", "GIT_COMMIT", "\"${gitCommit()}\"")
        buildConfigField("String", "GIT_BRANCH", "\"${gitBranch()}\"")
    }

    buildFeatures {
        buildConfig = true
    }

    flavorDimensions += "env"
    productFlavors {
        create("staging") {
            dimension = "env"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "BUILD_VARIANT", "\"staging\"")
            buildConfigField("String", "API_BASE_URL", "\"https://stagingapps.kkr.in\"")
            buildConfigField(
                "String",
                "IMAGE_BASE_URL",
                "\"https://stagingapps.kkr.in/static-assets/waf-images/\"",
            )
            buildConfigField("String", "IMAGE_VERSION", "\"1.89\"")
            buildConfigField("String", "APP_NAME", "\"KMPdemo (Staging)\"")
        }
        create("production") {
            dimension = "env"
            buildConfigField("String", "BUILD_VARIANT", "\"production\"")
            buildConfigField("String", "API_BASE_URL", "\"https://apps.kkr.in\"")
            buildConfigField(
                "String",
                "IMAGE_BASE_URL",
                "\"https://apps.kkr.in/static-assets/waf-images/\"",
            )
            buildConfigField("String", "IMAGE_VERSION", "\"1.89\"")
            buildConfigField("String", "APP_NAME", "\"KMPdemo\"")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
