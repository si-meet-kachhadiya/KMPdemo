package com.kmpdemo.mvi.listing.presentation

import com.kmpsdk.presentation.mvi.MviEffect

sealed interface ListingEffect : MviEffect {
    data class ShowMessage(val message: String) : ListingEffect
}
