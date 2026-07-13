package com.kmpdemo.listing.presentation

import com.kmpdemo.listing.ListingItem
import com.kmpsdk.KmpSdk.scope
import com.kmpsdk.platform.Cancellable
import com.kmpsdk.platform.asObservable
import com.kmpsdk.presentation.state.PaginatedDataState
import com.kmpsdk.presentation.state.itemsOrEmpty
import kotlinx.coroutines.CoroutineScope

/**
 * Swift-friendly snapshot of the listing screen state.
 * Keeps iOS UI code simple — no need to pattern-match Kotlin sealed classes in Swift.
 */
data class ListingUiSnapshot(
    val items: List<ListingItem>,
    val isLoading: Boolean,
    val errorMessage: String?,
    val hasMore: Boolean,
)

fun ListingState.toUiSnapshot(): ListingUiSnapshot {
    val listings = listings
    return ListingUiSnapshot(
        items = listings.itemsOrEmpty(),
        isLoading = listings.isLoading,
        errorMessage = when (listings) {
            is PaginatedDataState.Success,
            is PaginatedDataState.EndReached,
            is PaginatedDataState.LoadingMore,
            -> null

            else -> listings.toErrorMessage().ifBlank { null }
        },
        hasMore = (listings as? PaginatedDataState.Success)?.hasMore ?: false,
    )
}

/**
 * Small helper for SwiftUI — observe listing state and dispatch intents.
 */
class ListingController(scope: CoroutineScope) {
    private val viewModel = createListingViewModel(scope)

    fun observe(onChange: (ListingUiSnapshot) -> Unit): Cancellable =
        viewModel.state.asObservable().observe(scope) { state ->
            onChange(state.toUiSnapshot())
        }

    fun refresh() = viewModel.dispatch(ListingIntent.Refresh)

    fun loadMore() = viewModel.dispatch(ListingIntent.LoadMore)
}
