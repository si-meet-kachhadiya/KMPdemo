package com.kmpdemo.mvi.presentation

import com.kmpdemo.mvi.model.ListingItem
import com.kmpdemo.mvi.model.ListingItemDto

private const val IMAGE_CDN_BASE = "https://apps.kkr.in/"

fun ListingItemDto.toDomain(): ListingItem = ListingItem(
    id = assetId?.toString() ?: assetid ?: id ?: title.orEmpty(),
    title = assetTitle ?: assettitle ?: title ?: name ?: shortDesc?.trim() ?: "Untitled",
    imageUrl = resolveImageUrl(),
    publishDate = publishedDateApi ?: publishdate ?: publishDate ?: createdDateApi ?: createddate,
    assetType = assetTypeId?.toString() ?: assettype,
    likeCount = likecount ?: likes,
)

private fun ListingItemDto.resolveImageUrl(): String? {
    imageurl ?: imageUrl ?: thumbnail?.let { return it }
    if (!imagePath.isNullOrBlank() && !imageFileName.isNullOrBlank()) {
        return "$IMAGE_CDN_BASE$imagePath$imageFileName"
    }
    return null
}
