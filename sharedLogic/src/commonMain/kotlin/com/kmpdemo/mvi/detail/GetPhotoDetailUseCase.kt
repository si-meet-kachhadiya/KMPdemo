package com.kmpdemo.mvi.detail

import com.kmpdemo.mvi.detail.model.PhotoDetail
import com.kmpdemo.mvi.detail.model.PhotoDetailQuery
import com.kmpdemo.mvi.detail.model.PhotoDetailResponseDto
import com.kmpdemo.mvi.detail.presentation.toDomain
import com.kmpsdk.data.network.KmpNetworkClient
import com.kmpsdk.domain.error.KmpSdkResult

/**
 * Fetches photo album detail by [titleAlias].
 *
 * `GET /apiv3/photo/{titleAlias}?is_app=1`
 */
class GetPhotoDetailUseCase(
    private val networkClient: KmpNetworkClient,
) {
    suspend fun load(titleAlias: String): KmpSdkResult<PhotoDetail> {
        val path = PhotoDetailQuery.buildPath(titleAlias)

        return when (val result = networkClient.get<PhotoDetailResponseDto>(path)) {
            is KmpSdkResult.Success -> {
                val detail = result.data.toDomain()
                    ?: PhotoDetail(
                        albumId = "",
                        title = titleAlias,
                        titleAlias = titleAlias,
                        description = null,
                        publishedDate = null,
                        totalAssets = null,
                        images = emptyList(),
                    )
                KmpSdkResult.Success(detail)
            }

            is KmpSdkResult.Failure -> result
        }
    }
}
