package com.kmpdemo.mvi.listing.presentation

import com.kmpdemo.mvi.listing.GetListingsUseCase
import com.kmpsdk.KmpSdk
import com.kmpsdk.domain.error.KmpSdkResult
import com.kmpsdk.presentation.mvi.MviViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ListingViewModel(
    scope: CoroutineScope,
    private val getListingsUseCase: GetListingsUseCase,
) : MviViewModel<ListingState, ListingIntent, ListingEffect>(
    initialState = ListingState(),
    reducer = ListingReducer,
    scope = scope,
) {
    override fun dispatch(intent: ListingIntent) {
        super.dispatch(intent)
        when (intent) {
            ListingIntent.Load,
            ListingIntent.Refresh,
            -> fetchListings()

            ListingIntent.LoadStarted,
            is ListingIntent.LoadSucceeded,
            is ListingIntent.LoadFailed,
            -> Unit
        }
    }

    private fun fetchListings() {
        scope.launch {
            dispatch(ListingIntent.LoadStarted)

            when (val result = getListingsUseCase.load()) {
                is KmpSdkResult.Success ->
                    dispatch(ListingIntent.LoadSucceeded(result.data))

                is KmpSdkResult.Failure ->
                    dispatch(
                        ListingIntent.LoadFailed(
                            error = result.error,
                            offline = !KmpSdk.connectivityMonitor.isOnline(),
                        ),
                    )
            }
        }
    }
}

fun createListingViewModel(scope: CoroutineScope): ListingViewModel = ListingViewModel(
    scope = scope,
    getListingsUseCase = KmpSdk.get(),
)
