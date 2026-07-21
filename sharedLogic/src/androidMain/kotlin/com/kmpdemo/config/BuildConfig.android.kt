package com.kmpdemo.config

/**
 * Android Build Configuration Implementation.
 *
 * Values come from the host app's generated [com.kmpdemo.BuildConfig]
 * (product flavors + buildConfigField in `androidApp/build.gradle.kts`).
 *
 * Read at runtime so sharedLogic does not create a compile dependency on the app module.
 */
actual object BuildConfig {
    actual val buildVariant: String
        get() = HostBuildConfig.string("BUILD_VARIANT", fallback = "staging")

    actual val apiBaseUrl: String
        get() = HostBuildConfig.string(
            "API_BASE_URL",
            fallback = when (buildVariant) {
                "production" -> "https://apps.kkr.in"
                else -> "https://stagingapps.kkr.in"
            },
        )

    actual val imageBaseUrl: String
        get() = HostBuildConfig.string(
            "IMAGE_BASE_URL",
            fallback = "${apiBaseUrl.trimEnd('/')}/static-assets/waf-images/",
        )

    actual val imageVersion: String
        get() = HostBuildConfig.string("IMAGE_VERSION", fallback = "1.89")

    actual val appName: String
        get() = HostBuildConfig.string(
            "APP_NAME",
            fallback = when (buildVariant) {
                "production" -> "KMPdemo"
                else -> "KMPdemo (Staging)"
            },
        )

    actual val isDebug: Boolean
        get() = HostBuildConfig.bool("DEBUG", fallback = true)

    actual val isStaging: Boolean
        get() = buildVariant == "staging"

    actual val isProduction: Boolean
        get() = buildVariant == "production"

    actual val versionName: String
        get() = HostBuildConfig.string("VERSION_NAME", fallback = "1.0")

    actual val versionCode: Long
        get() = HostBuildConfig.long("VERSION_CODE", fallback = 1L)

    actual val buildTime: String
        get() = HostBuildConfig.string("BUILD_TIME", fallback = "local")

    actual val gitCommit: String
        get() = HostBuildConfig.string("GIT_COMMIT", fallback = "unknown")

    actual val gitBranch: String
        get() = HostBuildConfig.string("GIT_BRANCH", fallback = "unknown")

    actual val applicationId: String
        get() = HostBuildConfig.string("APPLICATION_ID", fallback = "com.kmpdemo")

    actual val buildType: String
        get() = HostBuildConfig.string("BUILD_TYPE", fallback = if (isDebug) "debug" else "release")
}

/**
 * Reads fields from the Android app's generated `com.kmpdemo.BuildConfig`.
 */
private object HostBuildConfig {
    private val hostClass: Class<*>? = try {
        Class.forName("com.kmpdemo.BuildConfig")
    } catch (_: ClassNotFoundException) {
        null
    }

    fun string(field: String, fallback: String): String =
        try {
            hostClass?.getField(field)?.get(null) as? String ?: fallback
        } catch (_: Throwable) {
            fallback
        }

    fun bool(field: String, fallback: Boolean): Boolean =
        try {
            hostClass?.getField(field)?.getBoolean(null) ?: fallback
        } catch (_: Throwable) {
            fallback
        }

    fun long(field: String, fallback: Long): Long =
        try {
            when (val value = hostClass?.getField(field)?.get(null)) {
                is Int -> value.toLong()
                is Long -> value
                is String -> value.toLongOrNull() ?: fallback
                else -> fallback
            }
        } catch (_: Throwable) {
            fallback
        }
}
