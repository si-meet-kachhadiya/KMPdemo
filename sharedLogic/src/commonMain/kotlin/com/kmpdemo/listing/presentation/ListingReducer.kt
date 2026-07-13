package com.kmpdemo.listing.presentation

import com.kmpsdk.presentation.mvi.MviReducer

/** Intents are handled in the ViewModel; reducer keeps the MVI contract simple. */
object ListingReducer : MviReducer<ListingState, ListingIntent> {
    override fun reduce(state: ListingState, intent: ListingIntent): ListingState = state
}
