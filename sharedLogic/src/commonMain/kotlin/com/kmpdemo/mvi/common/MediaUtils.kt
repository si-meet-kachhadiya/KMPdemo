package com.kmpdemo.mvi.common

import com.kmpdemo.config.BuildConfig

/**
 * Shared helpers used by listing, detail, and other MVI features.
 */
object MediaUtils {
    /**
     * Builds a full image URL from KKR config pattern:
     * `{imageBaseUrl}{image_path}{image_name}?v={imageVersion}`
     *
     * Example:
     * `https://stagingapps.kkr.in/static-assets/waf-images/e4/81/3a/0/EA2aTFjVGp.jpg?v=1.89`
     */
    fun buildImageUrl(imagePath: String?, imageFileName: String?): String? {
        if (imagePath.isNullOrBlank() || imageFileName.isNullOrBlank()) return null
        val base = BuildConfig.imageBaseUrl.let { if (it.endsWith("/")) it else "$it/" }
        val path = imagePath.trimStart('/')
        return "$base$path$imageFileName?v=${BuildConfig.imageVersion}"
    }

    fun stripSimpleHtml(value: String): String =
        value
            .replace(Regex("<[^>]*>"), "")
            .replace("&nbsp;", " ")
            .trim()
            .ifBlank { value }
}
