package com.kmpdemo.mvi.listing.presentation

import com.kmpdemo.mvi.common.MediaUtils
import com.kmpdemo.mvi.listing.model.ListingItem
import com.kmpdemo.mvi.listing.model.ListingItemDto

fun ListingItemDto.toDomain(): ListingItem = ListingItem(
    id = assetId?.toString() ?: assetid ?: id ?: title.orEmpty(),
    title = assetTitle ?: assettitle ?: title ?: name ?: shortDesc?.trim() ?: "Untitled",
    titleAlias = titleAlias,
    imageUrl = imageurl ?: imageUrl ?: thumbnail
        ?: MediaUtils.buildImageUrl(imagePath, imageFileName),
    publishDate = publishedDateApi ?: publishdate ?: publishDate ?: createdDateApi ?: createddate,
    assetType = assetTypeId?.toString() ?: assettype,
    likeCount = likecount ?: likes,
)
