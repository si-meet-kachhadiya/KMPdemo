package com.kmpdemo.mvi.detail.presentation

import com.kmpsdk.presentation.mvi.MviReducer
import com.kmpsdk.presentation.state.DataState

object PhotoDetailReducer : MviReducer<PhotoDetailState, PhotoDetailIntent> {
    override fun reduce(state: PhotoDetailState, intent: PhotoDetailIntent): PhotoDetailState =
        when (intent) {
            is PhotoDetailIntent.Load ->
                state.copy(titleAlias = intent.titleAlias)

            PhotoDetailIntent.Refresh -> state

            PhotoDetailIntent.LoadStarted ->
                state.copy(detail = DataState.Loading)

            is PhotoDetailIntent.LoadSucceeded ->
                state.copy(detail = DataState.Success(intent.detail))

            is PhotoDetailIntent.LoadFailed ->
                state.copy(
                    detail = if (intent.offline) {
                        DataState.NoNetwork
                    } else {
                        DataState.Failure(
                            requireNotNull(intent.error) { "LoadFailed requires error when online" },
                        )
                    },
                )
        }
}
