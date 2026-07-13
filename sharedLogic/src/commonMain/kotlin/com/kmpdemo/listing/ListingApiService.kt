package com.kmpdemo.listing

import com.kmpsdk.data.network.KmpNetworkClient
import com.kmpsdk.domain.error.KmpSdkResult
import com.kmpsdk.domain.pagination.PageRequest
import com.kmpsdk.domain.pagination.PaginatedResult
import com.kmpsdk.domain.repository.PaginatedRemoteDataSource

/**
 * Calls the KKR listing endpoint through the SDK network client.
 */
class ListingApiService(
    private val networkClient: KmpNetworkClient,
) : PaginatedRemoteDataSource<ListingItemDto> {

    override suspend fun fetchPage(request: PageRequest): KmpSdkResult<PaginatedResult<ListingItemDto>> {
        // API uses 1-based page numbers; SDK PageRequest is 0-based.
        val pageOneBased = request.page + 1
        val path = ListingQuery.buildPath(pageOneBased, request.pageSize)

        return when (val result = networkClient.get<ListingResponseDto>(path)) {
            is KmpSdkResult.Success -> {
                val items = result.data.extractItems()
                val total = result.data.totalRecords()
                val hasMore = when {
                    items.size < request.pageSize -> false
                    total != null -> (pageOneBased * request.pageSize) < total
                    else -> items.size >= request.pageSize
                }

                KmpSdkResult.Success(
                    PaginatedResult(
                        items = items,
                        page = request.page,
                        pageSize = request.pageSize,
                        hasMore = hasMore,
                    ),
                )
            }

            is KmpSdkResult.Failure -> result
        }
    }
}
