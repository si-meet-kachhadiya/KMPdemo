package com.kmpdemo.config

/**
 * Build Configuration - Common expect interface.
 * Platform-specific actuals provide values (Android Gradle BuildConfig / iOS Info.plist).
 */
expect object BuildConfig {
    /** Build variant: `"staging"` or `"production"`. */
    val buildVariant: String

    /** Base URL for REST API (e.g. `https://stagingapps.kkr.in`). */
    val apiBaseUrl: String

    /**
     * Image CDN base ending with `/`, e.g.
     * `https://stagingapps.kkr.in/static-assets/waf-images/`
     */
    val imageBaseUrl: String

    /** Cache-busting query for images, e.g. `1.89`. */
    val imageVersion: String

    /** App name (can vary by variant). */
    val appName: String

    /** Whether this is a debug build. */
    val isDebug: Boolean

    /** Whether this is a staging build. */
    val isStaging: Boolean

    /** Whether this is a production build. */
    val isProduction: Boolean

    /** User-visible version, e.g. `1.0.0-staging`. */
    val versionName: String

    /** Monotonic build number (Android versionCode / iOS CFBundleVersion). */
    val versionCode: Long

    /** When this binary was built (Gradle / Xcode embed). */
    val buildTime: String

    /** Short git commit at build time. */
    val gitCommit: String

    /** Git branch at build time. */
    val gitBranch: String

    /** Application / bundle identifier. */
    val applicationId: String

    /** `debug` or `release`. */
    val buildType: String
}
