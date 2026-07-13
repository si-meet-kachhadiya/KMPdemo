package com.kmpdemo.mvvm.presentation

import com.kmpdemo.listing.ListingItem
import com.kmpsdk.presentation.mvi.MviState
import com.kmpsdk.presentation.state.DataState

data class ListingState(
    val listings: DataState<List<ListingItem>> = DataState.Idle,
) : MviState
