package com.kmpdemo.mvi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class ListingPaginationDto(
    val total: Int? = null,
    @SerialName("current_page") val currentPage: Int? = null,
)

@Serializable
data class ListingContentDto(
    val items: List<ListingItemDto>? = null,
    val pagination: ListingPaginationDto? = null,
)

@Serializable
data class ListingMetaDto(
    val pagination: ListingPaginationDto? = null,
)

/**
 * API response wrapper for `/apiv4/listing`.
 *
 * KKR returns items under `content.items`; root `data` is often null.
 */
@Serializable
data class ListingResponseDto(
    val status: Int? = null,
    @SerialName("ApplicationDomain") val applicationDomain: String? = null,
    val content: ListingContentDto? = null,
    val meta: ListingMetaDto? = null,
    val listing: List<ListingItemDto>? = null,
    val data: JsonElement? = null,
    val items: List<ListingItemDto>? = null,
    @SerialName("totalcount") val totalCountSnake: Int? = null,
    val totalCount: Int? = null,
    val pgnum: Int? = null,
    val pgsize: Int? = null,
    val message: String? = null,
) {
    fun extractItems(): List<ListingItemDto> {
        content?.items?.let { return it }
        listing?.let { return it }
        items?.let { return it }

        data?.let { element ->
            when (element) {
                is JsonArray -> return decodeItemsFromArray(element)
                is JsonObject -> {
                    (element["listing"] as? JsonArray)?.let { return decodeItemsFromArray(it) }
                    (element["items"] as? JsonArray)?.let { return decodeItemsFromArray(it) }
                    (element["data"] as? JsonArray)?.let { return decodeItemsFromArray(it) }
                    (element["content"] as? JsonObject)?.get("items")?.let { itemsElement ->
                        (itemsElement as? JsonArray)?.let { return decodeItemsFromArray(it) }
                    }
                }

                else -> {}
            }
        }

        return emptyList()
    }

    fun totalRecords(): Int? =
        content?.pagination?.total
            ?: meta?.pagination?.total
            ?: totalCountSnake
            ?: totalCount

    private fun decodeItemsFromArray(array: JsonArray): List<ListingItemDto> =
        runCatching {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }.decodeFromJsonElement(
                kotlinx.serialization.serializer<List<ListingItemDto>>(),
                array,
            )
        }.getOrDefault(emptyList())
}

/**
 * Raw listing item from the KKR `/apiv4/listing` API.
 */
@Serializable
data class ListingItemDto(
    @SerialName("asset_id") val assetId: Int? = null,
    val assetid: String? = null,
    val id: String? = null,
    @SerialName("asset_title") val assetTitle: String? = null,
    val assettitle: String? = null,
    val title: String? = null,
    val name: String? = null,
    @SerialName("short_desc") val shortDesc: String? = null,
    @SerialName("asset_type_id") val assetTypeId: Int? = null,
    val assettype: String? = null,
    @SerialName("image_path") val imagePath: String? = null,
    @SerialName("image_file_name") val imageFileName: String? = null,
    val imageurl: String? = null,
    val imageUrl: String? = null,
    val thumbnail: String? = null,
    @SerialName("published_date") val publishedDateApi: String? = null,
    val publishdate: String? = null,
    val publishDate: String? = null,
    @SerialName("created_date") val createdDateApi: String? = null,
    val createddate: String? = null,
    val likecount: String? = null,
    val likes: String? = null,
)
