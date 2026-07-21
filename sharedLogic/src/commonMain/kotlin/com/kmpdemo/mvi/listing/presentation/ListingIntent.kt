package com.kmpdemo.mvi.listing.presentation

import com.kmpdemo.mvi.listing.model.ListingItem
import com.kmpsdk.domain.error.KmpSdkError
import com.kmpsdk.presentation.mvi.MviIntent

sealed interface ListingIntent : MviIntent {
    data object Load : ListingIntent
    data object Refresh : ListingIntent
    data object LoadStarted : ListingIntent
    data class LoadSucceeded(val items: List<ListingItem>) : ListingIntent
    data class LoadFailed(
        val error: KmpSdkError? = null,
        val offline: Boolean = false,
    ) : ListingIntent
}
