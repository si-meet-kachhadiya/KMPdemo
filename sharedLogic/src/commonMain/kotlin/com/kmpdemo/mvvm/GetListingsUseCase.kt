package com.kmpdemo.mvvm

import com.kmpdemo.mvi.listing.model.ListingItem
import com.kmpdemo.mvi.listing.model.ListingQuery
import com.kmpdemo.mvi.listing.model.ListingResponseDto
import com.kmpdemo.mvi.listing.presentation.toDomain
import com.kmpsdk.data.network.KmpNetworkClient
import com.kmpsdk.domain.error.KmpSdkResult

/** Fetches page 2 of the KKR listing API (Path A — online-only). */
class GetListingsUseCase(
    private val networkClient: KmpNetworkClient,
) {
    suspend fun load(): KmpSdkResult<List<ListingItem>> {
        val path = ListingQuery.buildPath(
            pageOneBased = DEFAULT_PAGE,
            pageSize = ListingQuery.DEFAULT_PAGE_SIZE,
        )

        return when (val result = networkClient.get<ListingResponseDto>(path)) {
            is KmpSdkResult.Success -> KmpSdkResult.Success(
                result.data.extractItems().map { it.toDomain() },
            )
            is KmpSdkResult.Failure -> result
        }
    }

    private companion object {
        const val DEFAULT_PAGE = 2
    }
}
