package com.kmpdemo.mvi.detail.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API response for photo detail:
 * `GET /apiv3/photo/{titleAlias}?is_app=1`
 */
@Serializable
data class PhotoDetailResponseDto(
    val status: Int? = null,
    val content: PhotoDetailContentDto? = null,
    val message: String? = null,
)

@Serializable
data class PhotoDetailContentDto(
    val data: PhotoDetailDataDto? = null,
)

@Serializable
data class PhotoDetailDataDto(
    val title: String? = null,
    @SerialName("slug_url") val slugUrl: String? = null,
    @SerialName("title_alias") val titleAlias: String? = null,
    @SerialName("album_id") val albumId: Int? = null,
    @SerialName("album_desc") val albumDesc: String? = null,
    @SerialName("short_title") val shortTitle: String? = null,
    @SerialName("asset_type") val assetType: String? = null,
    @SerialName("total_assets") val totalAssets: String? = null,
    @SerialName("published_date") val publishedDate: String? = null,
    @SerialName("modified_date") val modifiedDate: String? = null,
    val images: List<PhotoImageWrapperDto>? = null,
    val entitydata: List<PhotoEntityDto>? = null,
)

@Serializable
data class PhotoImageWrapperDto(
    val data: PhotoImageDto? = null,
)

@Serializable
data class PhotoImageDto(
    @SerialName("image_id") val imageId: String? = null,
    @SerialName("imageName") val imageName: String? = null,
    @SerialName("imagePath") val imagePath: String? = null,
    @SerialName("image_alt") val imageAlt: String? = null,
    @SerialName("image_caption") val imageCaption: String? = null,
    @SerialName("image_desc") val imageDesc: String? = null,
    @SerialName("is_cover") val isCover: String? = null,
    @SerialName("order_number") val orderNumber: String? = null,
    val status: String? = null,
)

@Serializable
data class PhotoEntityDto(
    val name: String? = null,
    @SerialName("ent_disp_name") val displayName: String? = null,
    @SerialName("entity_type_name") val entityTypeName: String? = null,
    @SerialName("entity_role_map_id") val entityRoleMapId: Int? = null,
)
