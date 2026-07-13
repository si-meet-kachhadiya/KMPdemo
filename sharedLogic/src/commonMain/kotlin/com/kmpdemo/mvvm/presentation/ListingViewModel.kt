package com.kmpdemo.mvvm.presentation

import com.kmpdemo.mvvm.GetListingsUseCase
import com.kmpsdk.KmpSdk
import com.kmpsdk.domain.error.KmpSdkResult
import com.kmpsdk.presentation.mvi.MviEffect
import com.kmpsdk.presentation.mvi.MviViewModel
import com.kmpsdk.presentation.state.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed interface ListingEffect : MviEffect

class ListingViewModel(
    scope: CoroutineScope,
    private val getListingsUseCase: GetListingsUseCase,
) : MviViewModel<ListingState, ListingIntent, ListingEffect>(
    initialState = ListingState(),
    reducer = ListingReducer,
    scope = scope,
) {
    init {
        loadListings(showLoading = true)
    }

    override fun dispatch(intent: ListingIntent) {
        super.dispatch(intent)
        if (intent == ListingIntent.Refresh) {
            loadListings(showLoading = true)
        }
    }

    private fun loadListings(showLoading: Boolean) {
        scope.launch {
            if (showLoading) {
                setState { it.copy(listings = DataState.Loading) }
            }

            when (val result = getListingsUseCase.load()) {
                is KmpSdkResult.Success ->
                    setState { it.copy(listings = DataState.Success(result.data)) }

                is KmpSdkResult.Failure ->
                    setState {
                        it.copy(
                            listings = if (!KmpSdk.connectivityMonitor.isOnline()) {
                                DataState.NoNetwork
                            } else {
                                DataState.Failure(result.error)
                            },
                        )
                    }
            }
        }
    }
}

fun createListingViewModel(scope: CoroutineScope): ListingViewModel = ListingViewModel(
    scope = scope,
    getListingsUseCase = KmpSdk.get(),
)
