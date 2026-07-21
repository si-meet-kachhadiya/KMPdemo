package com.kmpdemo.mvi.detail.presentation

import com.kmpdemo.mvi.detail.model.PhotoDetail
import com.kmpsdk.presentation.mvi.MviState
import com.kmpsdk.presentation.state.DataState

data class PhotoDetailState(
    val titleAlias: String = "",
    val detail: DataState<PhotoDetail> = DataState.Idle,
) : MviState
