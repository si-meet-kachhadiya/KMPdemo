package com.kmpdemo.mvi.listing.presentation

import com.kmpsdk.presentation.mvi.MviReducer
import com.kmpsdk.presentation.state.DataState

object ListingReducer : MviReducer<ListingState, ListingIntent> {
    override fun reduce(state: ListingState, intent: ListingIntent): ListingState = when (intent) {
        ListingIntent.Load,
        ListingIntent.Refresh,
        -> state

        ListingIntent.LoadStarted ->
            state.copy(listings = DataState.Loading)

        is ListingIntent.LoadSucceeded ->
            state.copy(listings = DataState.Success(intent.items))

        is ListingIntent.LoadFailed ->
            state.copy(
                listings = if (intent.offline) {
                    DataState.NoNetwork
                } else {
                    DataState.Failure(requireNotNull(intent.error) { "LoadFailed requires error when online" })
                },
            )
    }
}
