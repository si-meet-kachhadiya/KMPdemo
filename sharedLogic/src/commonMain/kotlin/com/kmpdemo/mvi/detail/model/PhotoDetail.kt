package com.kmpdemo.mvi.detail.model

/**
 * Domain model for photo album detail screen.
 */
data class PhotoDetail(
    val albumId: String,
    val title: String,
    val titleAlias: String,
    val description: String?,
    val publishedDate: String?,
    val totalAssets: Int?,
    val images: List<PhotoImage>,
)

data class PhotoImage(
    val id: String,
    val imageUrl: String?,
    val caption: String?,
    val isCover: Boolean,
    val order: Int,
)
