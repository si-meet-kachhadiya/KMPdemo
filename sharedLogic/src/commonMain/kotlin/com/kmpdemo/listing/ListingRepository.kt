package com.kmpdemo.listing

import com.kmpsdk.core.di.KmpSdkContext
import com.kmpsdk.data.repository.BasePaginatedRepository

/**
 * Paginated repository backed by the SDK base class.
 * ViewModel talks to this directly — no separate UseCase layer.
 */
class ListingRepository(
    localDataSource: ListingLocalDataSource,
    apiService: ListingApiService,
    ctx: KmpSdkContext,
) : BasePaginatedRepository<ListingItem, ListingItemDto>(
    tag = "ListingRepository",
    pageSize = ListingQuery.DEFAULT_PAGE_SIZE,
    observeLocal = localDataSource::observeAll,
    countLocal = localDataSource::count,
    replaceAll = localDataSource::replaceAll,
    appendPage = localDataSource::appendAll,
    remote = apiService,
    mapDto = { dto, _ -> dto.toDomain() },
    connectivityMonitor = ctx.connectivityMonitor,
    syncPolicy = ctx.config.syncPolicy,
    logger = ctx.logger,
)
