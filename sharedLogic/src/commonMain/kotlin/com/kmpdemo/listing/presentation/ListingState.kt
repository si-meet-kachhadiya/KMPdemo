package com.kmpdemo.listing.presentation

import com.kmpdemo.listing.ListingItem
import com.kmpsdk.presentation.mvi.MviState
import com.kmpsdk.presentation.state.PaginatedDataState

data class ListingState(
    val listings: PaginatedDataState<ListingItem> = PaginatedDataState.Idle,
) : MviState
