package com.kmpdemo.mvi.presentation

import com.kmpdemo.mvi.model.ListingItem
import com.kmpsdk.KmpSdk.scope
import com.kmpsdk.platform.Cancellable
import com.kmpsdk.platform.asObservable
import com.kmpsdk.presentation.state.DataState
import com.kmpsdk.presentation.state.toErrorMessage
import kotlinx.coroutines.CoroutineScope

/**
 * Swift-friendly snapshot of the MVI listing screen.
 * iOS does not need to handle Kotlin [DataState] sealed types in SwiftUI.
 */
data class MviListingUiSnapshot(
    val items: List<ListingItem>,
    val isLoading: Boolean,
    val errorMessage: String?,
)

fun ListingState.toMviUiSnapshot(): MviListingUiSnapshot {
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

/**
 * iOS entry point — observe listings and trigger refresh via MVI intents.
 * Use from SwiftUI instead of calling the API directly.
 */
class MviListingController(scope: CoroutineScope) {
    private val viewModel = createListingViewModel(scope)

    init {
        // Same as MainActivity — start API load when controller is created
        viewModel.dispatch(ListingIntent.Load)
    }

    fun observeListingAPI(onChange: (MviListingUiSnapshot) -> Unit): Cancellable =
        viewModel.state.asObservable().observe(scope) { state ->
            onChange(state.toMviUiSnapshot())
        }

    fun refresh() = viewModel.dispatch(ListingIntent.Refresh)
}
