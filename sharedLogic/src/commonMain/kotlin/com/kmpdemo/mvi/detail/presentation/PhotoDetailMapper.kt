package com.kmpdemo.mvi.detail.presentation

import com.kmpdemo.mvi.common.MediaUtils
import com.kmpdemo.mvi.detail.model.PhotoDetail
import com.kmpdemo.mvi.detail.model.PhotoDetailDataDto
import com.kmpdemo.mvi.detail.model.PhotoDetailResponseDto
import com.kmpdemo.mvi.detail.model.PhotoImage
import com.kmpdemo.mvi.detail.model.PhotoImageDto

fun PhotoDetailResponseDto.toDomain(): PhotoDetail? {
    val data = content?.data ?: return null
    return data.toDomain()
}

fun PhotoDetailDataDto.toDomain(): PhotoDetail {
    val alias = slugUrl ?: titleAlias ?: ""
    return PhotoDetail(
        albumId = albumId?.toString().orEmpty(),
        title = title?.trim().orEmpty().ifBlank { "Untitled" },
        titleAlias = alias,
        description = albumDesc?.let(MediaUtils::stripSimpleHtml),
        publishedDate = publishedDate,
        totalAssets = totalAssets?.toIntOrNull(),
        images = images
            .orEmpty()
            .mapNotNull { wrapper -> wrapper.data?.toDomain() }
            .sortedBy { it.order },
    )
}

fun PhotoImageDto.toDomain(): PhotoImage = PhotoImage(
    id = imageId.orEmpty(),
    imageUrl = MediaUtils.buildImageUrl(imagePath, imageName),
    caption = imageCaption?.takeIf { it.isNotBlank() }
        ?: imageDesc?.takeIf { it.isNotBlank() }
        ?: imageAlt?.takeIf { it.isNotBlank() },
    isCover = isCover == "1",
    order = orderNumber?.toIntOrNull() ?: 0,
)
