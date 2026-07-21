package com.kmpdemo.mvi.listing.model

/**
 * Domain model shown in Android/iOS listing UI.
 */
data class ListingItem(
    val id: String,
    val title: String,
    /** Slug used for photo detail API: `/apiv3/photo/{titleAlias}?is_app=1` */
    val titleAlias: String?,
    val imageUrl: String?,
    val publishDate: String?,
    val assetType: String?,
    val likeCount: String?,
)
