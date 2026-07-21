package com.kmpdemo.mvi.detail.presentation

import com.kmpsdk.presentation.mvi.MviEffect

sealed interface PhotoDetailEffect : MviEffect {
    data class ShowMessage(val message: String) : PhotoDetailEffect
}
