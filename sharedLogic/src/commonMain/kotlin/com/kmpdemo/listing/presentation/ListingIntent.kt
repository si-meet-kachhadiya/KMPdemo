package com.kmpdemo.listing.presentation

import com.kmpsdk.presentation.mvi.MviIntent

sealed interface ListingIntent : MviIntent {
    /** Reload from page 1. */
    data object Refresh : ListingIntent

    /** Load the next page when the user scrolls near the end. */
    data object LoadMore : ListingIntent
}
