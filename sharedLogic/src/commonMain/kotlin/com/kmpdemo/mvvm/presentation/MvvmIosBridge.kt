package com.kmpdemo.mvvm.presentation

import com.kmpdemo.listing.ListingItem
import com.kmpsdk.KmpSdk.scope
import com.kmpsdk.platform.Cancellable
import com.kmpsdk.platform.asObservable
import com.kmpsdk.presentation.state.DataState
import com.kmpsdk.presentation.state.toErrorMessage
import kotlinx.coroutines.CoroutineScope

/**
 * Swift-friendly snapshot of the MVVM listing screen.
 * iOS does not need to handle Kotlin [DataState] sealed types in SwiftUI.
 */
data class MvvmListingUiSnapshot(
    val items: List<ListingItem>,
    val isLoading: Boolean,
    val errorMessage: String?,
)

fun ListingState.toMvvmUiSnapshot(): MvvmListingUiSnapshot {
    val listings = listings
    return MvvmListingUiSnapshot(
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
 * iOS entry point — observe listings and trigger refresh.
 * Use from SwiftUI instead of calling the API directly.
 */
class MvvmListingController(scope: CoroutineScope) {
    private val viewModel = createListingViewModel(scope)

    fun observe(onChange: (MvvmListingUiSnapshot) -> Unit): Cancellable =
        viewModel.state.asObservable().observe(scope) { state ->
            onChange(state.toMvvmUiSnapshot())
        }

    fun refresh() = viewModel.dispatch(ListingIntent.Refresh)
}
