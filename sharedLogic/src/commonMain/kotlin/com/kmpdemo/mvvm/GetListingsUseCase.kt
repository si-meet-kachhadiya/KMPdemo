package com.kmpdemo.mvvm

import com.kmpdemo.listing.ListingItem
import com.kmpdemo.listing.ListingQuery
import com.kmpdemo.listing.ListingResponseDto
import com.kmpdemo.listing.toDomain
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
