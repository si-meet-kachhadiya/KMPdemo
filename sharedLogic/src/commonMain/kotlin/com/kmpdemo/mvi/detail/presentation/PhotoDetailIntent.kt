package com.kmpdemo.mvi.detail.presentation

import com.kmpdemo.mvi.detail.model.PhotoDetail
import com.kmpsdk.domain.error.KmpSdkError
import com.kmpsdk.presentation.mvi.MviIntent

sealed interface PhotoDetailIntent : MviIntent {
    data class Load(val titleAlias: String) : PhotoDetailIntent
    data object Refresh : PhotoDetailIntent
    data object LoadStarted : PhotoDetailIntent
    data class LoadSucceeded(val detail: PhotoDetail) : PhotoDetailIntent
    data class LoadFailed(
        val error: KmpSdkError? = null,
        val offline: Boolean = false,
    ) : PhotoDetailIntent
}
