package com.kmpdemo.mvvm.presentation

import com.kmpsdk.presentation.mvi.MviIntent

sealed interface ListingIntent : MviIntent {
    data object Refresh : ListingIntent
}
