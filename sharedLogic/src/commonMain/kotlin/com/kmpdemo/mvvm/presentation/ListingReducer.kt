package com.kmpdemo.mvvm.presentation

import com.kmpsdk.presentation.mvi.MviReducer

object ListingReducer : MviReducer<ListingState, ListingIntent> {
    override fun reduce(state: ListingState, intent: ListingIntent): ListingState = state
}
