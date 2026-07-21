package com.kmpdemo.mvi.detail.model

/**
 * Builds the photo detail path.
 * Example: `/apiv3/photo/moments-from-adkr-vs-mie-qualifier-2?is_app=1`
 */
object PhotoDetailQuery {
    const val PHOTO_PATH = "/apiv3/photo"

    fun buildPath(titleAlias: String, isApp: Boolean = true): String {
        val slug = titleAlias.trim().trim('/')
        require(slug.isNotEmpty()) { "titleAlias must not be blank" }
        return "$PHOTO_PATH/$slug?is_app=${if (isApp) 1 else 0}"
    }
}
