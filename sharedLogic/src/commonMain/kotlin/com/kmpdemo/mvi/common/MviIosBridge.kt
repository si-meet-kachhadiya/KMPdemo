package com.kmpdemo.mvi.common

import com.kmpdemo.mvi.detail.model.PhotoImage
import com.kmpdemo.mvi.detail.presentation.PhotoDetailIntent
import com.kmpdemo.mvi.detail.presentation.PhotoDetailState
import com.kmpdemo.mvi.detail.presentation.createPhotoDetailViewModel
import com.kmpdemo.mvi.listing.model.ListingItem
import com.kmpdemo.mvi.listing.presentation.ListingIntent
import com.kmpdemo.mvi.listing.presentation.ListingState
import com.kmpdemo.mvi.listing.presentation.createListingViewModel
import com.kmpsdk.platform.Cancellable
import com.kmpsdk.platform.asObservable
import com.kmpsdk.presentation.state.DataState
import com.kmpsdk.presentation.state.toErrorMessage
import kotlinx.coroutines.CoroutineScope

/**
 * Swift-friendly listing snapshot (no Kotlin sealed-class handling in SwiftUI).
 */
data class MviListingUiSnapshot(
    val items: List<ListingItem>,
    val isLoading: Boolean,
    val errorMessage: String?,
)

/**
 * Swift-friendly photo-detail snapshot.
 */
data class MviPhotoDetailUiSnapshot(
    val title: String?,
    val titleAlias: String?,
    val description: String?,
    val publishedDate: String?,
    val images: List<PhotoImage>,
    val isLoading: Boolean,
    val errorMessage: String?,
)

private fun ListingState.toUiSnapshot(): MviListingUiSnapshot {
    val listings = listings
    return MviListingUiSnapshot(
        items = when (listings) {
            is DataState.Success -> listings.data
            else -> emptyList()
        },
        isLoading = listings is DataState.Loading,
        errorMessage = when (listings) {
            is DataState.Idle -> null
            is DataState.Loading -> null
            is DataState.Success -> null
            is DataState.Failure -> listings.toErrorMessage().ifBlank { null }
            is DataState.NoNetwork -> "No internet connection"
        },
    )
}

private fun PhotoDetailState.toUiSnapshot(): MviPhotoDetailUiSnapshot {
    val detail = detail
    return MviPhotoDetailUiSnapshot(
        title = (detail as? DataState.Success)?.data?.title,
        titleAlias = titleAlias.ifBlank { null }
            ?: (detail as? DataState.Success)?.data?.titleAlias,
        description = (detail as? DataState.Success)?.data?.description,
        publishedDate = (detail as? DataState.Success)?.data?.publishedDate,
        images = when (detail) {
            is DataState.Success -> detail.data.images
            else -> emptyList()
        },
        isLoading = detail is DataState.Loading,
        errorMessage = when (detail) {
            is DataState.Idle -> null
            is DataState.Loading -> null
            is DataState.Success -> null
            is DataState.Failure -> detail.toErrorMessage().ifBlank { null }
            is DataState.NoNetwork -> "No internet connection"
        },
    )
}

/**
 * Single iOS entry point for all MVI APIs.
 *
 * iOS creates one controller and uses listing / photo-detail methods as needed.
 *
 * ```swift
 * let api = MviApiController(scope: KmpSdk.shared.scope)
 * api.observeListings { snapshot in ... }
 * api.loadPhotoDetail(titleAlias: "moments-from-...")
 * api.observePhotoDetail { snapshot in ... }
 * ```
 */
class MviApiController(
    private val scope: CoroutineScope,
) {
    private val listingViewModel = createListingViewModel(scope)
    private val photoDetailViewModel = createPhotoDetailViewModel(scope)

    // ── Listing ──────────────────────────────────────────────────────────

    fun loadListings() = listingViewModel.dispatch(ListingIntent.Load)

    fun refreshListings() = listingViewModel.dispatch(ListingIntent.Refresh)

    fun observeListings(onChange: (MviListingUiSnapshot) -> Unit): Cancellable =
        listingViewModel.state.asObservable().observe(scope) { state ->
            onChange(state.toUiSnapshot())
        }

    // ── Photo detail ─────────────────────────────────────────────────────

    fun loadPhotoDetail(titleAlias: String) =
        photoDetailViewModel.dispatch(PhotoDetailIntent.Load(titleAlias))

    fun refreshPhotoDetail() =
        photoDetailViewModel.dispatch(PhotoDetailIntent.Refresh)

    fun observePhotoDetail(onChange: (MviPhotoDetailUiSnapshot) -> Unit): Cancellable =
        photoDetailViewModel.state.asObservable().observe(scope) { state ->
            onChange(state.toUiSnapshot())
        }
}
