package com.kmpdemo.mvvm.presentation

import com.kmpdemo.mvi.listing.model.ListingItem
import com.kmpsdk.presentation.mvi.MviState
import com.kmpsdk.presentation.state.DataState

data class ListingState(
    val listings: DataState<List<ListingItem>> = DataState.Idle,
) : MviState
