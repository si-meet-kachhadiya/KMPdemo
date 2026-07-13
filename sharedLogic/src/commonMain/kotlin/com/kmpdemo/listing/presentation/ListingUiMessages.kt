package com.kmpdemo.listing.presentation

import com.kmpsdk.domain.error.httpStatusCode
import com.kmpsdk.presentation.state.PaginatedDataState

/** User-friendly error text for listing UI on Android and iOS. */
fun PaginatedDataState<*>.toErrorMessage(): String = when (this) {
    is PaginatedDataState.Idle -> "Pull to refresh or tap Refresh to load listings"
    is PaginatedDataState.Loading -> "Loading listings..."
    is PaginatedDataState.LoadingMore -> "Loading more..."
    is PaginatedDataState.Success -> ""
    is PaginatedDataState.EndReached -> "All listings loaded"
    is PaginatedDataState.NoNetwork -> "No internet connection"
    is PaginatedDataState.Failure -> {
        val code = error.httpStatusCode
        if (code != null) "${error.message} (HTTP $code)" else error.message
    }
}
