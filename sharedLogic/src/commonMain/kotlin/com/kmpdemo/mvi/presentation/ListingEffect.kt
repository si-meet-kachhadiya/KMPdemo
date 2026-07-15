package com.kmpdemo.mvi.presentation

import com.kmpsdk.presentation.mvi.MviEffect

/**
 * One-shot UI events (toast / navigation) — not part of durable screen state.
 */
sealed interface ListingEffect : MviEffect {
    data class ShowMessage(val message: String) : ListingEffect
}
