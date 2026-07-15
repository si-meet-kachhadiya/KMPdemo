package com.kmpdemo.mvi.presentation

import com.kmpsdk.presentation.mvi.MviReducer
import com.kmpsdk.presentation.state.DataState

/**
 * Pure state machine — no API / coroutine calls.
 * All listing UI state transitions happen here from [ListingIntent].
 */
object ListingReducer : MviReducer<ListingState, ListingIntent> {
    override fun reduce(state: ListingState, intent: ListingIntent): ListingState = when (intent) {
        // User intents — side effects run in ViewModel; state may stay unchanged until results arrive.
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
