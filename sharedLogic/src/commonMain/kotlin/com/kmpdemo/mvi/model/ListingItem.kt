package com.kmpdemo.mvi.model

/**
 * Domain model shown in Android/iOS UI.
 * Mapped from [ListingItemDto] in [ListingMapper].
 */
data class ListingItem(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val publishDate: String?,
    val assetType: String?,
    val likeCount: String?,
)