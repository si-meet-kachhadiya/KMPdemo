package com.kmpdemo.mvi.detail.presentation

import com.kmpdemo.mvi.detail.GetPhotoDetailUseCase
import com.kmpsdk.KmpSdk
import com.kmpsdk.domain.error.KmpSdkResult
import com.kmpsdk.presentation.mvi.MviViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PhotoDetailViewModel(
    scope: CoroutineScope,
    private val getPhotoDetailUseCase: GetPhotoDetailUseCase,
) : MviViewModel<PhotoDetailState, PhotoDetailIntent, PhotoDetailEffect>(
    initialState = PhotoDetailState(),
    reducer = PhotoDetailReducer,
    scope = scope,
) {
    override fun dispatch(intent: PhotoDetailIntent) {
        super.dispatch(intent)
        when (intent) {
            is PhotoDetailIntent.Load -> fetchDetail(intent.titleAlias)
            PhotoDetailIntent.Refresh -> {
                val alias = state.value.titleAlias
                if (alias.isNotBlank()) fetchDetail(alias)
            }

            PhotoDetailIntent.LoadStarted,
            is PhotoDetailIntent.LoadSucceeded,
            is PhotoDetailIntent.LoadFailed,
            -> Unit
        }
    }

    private fun fetchDetail(titleAlias: String) {
        scope.launch {
            dispatch(PhotoDetailIntent.LoadStarted)

            when (val result = getPhotoDetailUseCase.load(titleAlias)) {
                is KmpSdkResult.Success ->
                    dispatch(PhotoDetailIntent.LoadSucceeded(result.data))

                is KmpSdkResult.Failure ->
                    dispatch(
                        PhotoDetailIntent.LoadFailed(
                            error = result.error,
                            offline = !KmpSdk.connectivityMonitor.isOnline(),
                        ),
                    )
            }
        }
    }
}

fun createPhotoDetailViewModel(scope: CoroutineScope): PhotoDetailViewModel =
    PhotoDetailViewModel(
        scope = scope,
        getPhotoDetailUseCase = KmpSdk.get(),
    )
