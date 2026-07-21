package com.kmpdemo.config

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.Foundation.NSBundle

/**
 * iOS Build Configuration Implementation.
 *
 * Set values via:
 * 1. Xcode Build Settings / Info.plist (preferred) — add `BUILD_VARIANT`
 * 2. Environment variables (Xcode scheme)
 * 3. Default below (staging)
 *
 * Create Xcode schemes "Staging" / "Production" and set BUILD_VARIANT per scheme.
 */
@OptIn(ExperimentalForeignApi::class)
actual object BuildConfig {
    actual val buildVariant: String
        get() {
            val variantFromPlist = NSBundle.mainBundle
                .objectForInfoDictionaryKey("BUILD_VARIANT") as? String
            if (variantFromPlist != null) return variantFromPlist

            val variantFromEnv = platform.posix.getenv("BUILD_VARIANT")
            if (variantFromEnv != null) return variantFromEnv.toKString()

            return "staging"
        }

    actual val apiBaseUrl: String
        get() = when (buildVariant) {
            "production" -> "https://apps.kkr.in"
            else -> "https://stagingapps.kkr.in"
        }

    actual val imageBaseUrl: String
        get() {
            val fromPlist = NSBundle.mainBundle
                .objectForInfoDictionaryKey("IMAGE_BASE_URL") as? String
            if (!fromPlist.isNullOrBlank()) return fromPlist.ensureTrailingSlash()
            return "${apiBaseUrl.trimEnd('/')}/static-assets/waf-images/"
        }

    actual val imageVersion: String
        get() = NSBundle.mainBundle
            .objectForInfoDictionaryKey("IMAGE_VERSION") as? String
            ?: "1.89"

    actual val appName: String
        get() = when (buildVariant) {
            "production" -> "KMPdemo"
            else -> "KMPdemo (Staging)"
        }

    actual val isDebug: Boolean
        get() = buildVariant == "staging"

    actual val isStaging: Boolean
        get() = buildVariant == "staging"

    actual val isProduction: Boolean
        get() = buildVariant == "production"

    actual val versionName: String
        get() = NSBundle.mainBundle
            .objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "?"

    actual val versionCode: Long
        get() = (NSBundle.mainBundle
            .objectForInfoDictionaryKey("CFBundleVersion") as? String)
            ?.toLongOrNull() ?: 0L

    actual val buildTime: String
        get() = NSBundle.mainBundle
            .objectForInfoDictionaryKey("BUILD_TIME") as? String
            ?: "Local Xcode build (BUILD_TIME not in Info.plist)"

    actual val gitCommit: String
        get() = NSBundle.mainBundle
            .objectForInfoDictionaryKey("GIT_COMMIT") as? String ?: "unknown"

    actual val gitBranch: String
        get() = NSBundle.mainBundle
            .objectForInfoDictionaryKey("GIT_BRANCH") as? String ?: "unknown"

    actual val applicationId: String
        get() = NSBundle.mainBundle.bundleIdentifier ?: "unknown"

    actual val buildType: String
        get() = if (isDebug) "debug" else "release"
}

private fun String.ensureTrailingSlash(): String =
    if (endsWith("/")) this else "$this/"

