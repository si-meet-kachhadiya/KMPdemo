package com.kmpdemo.listing.presentation

import com.kmpdemo.listing.ListingQuery
import com.kmpdemo.listing.ListingRepository
import com.kmpsdk.KmpSdk
import com.kmpsdk.presentation.binding.PaginatedListController
import com.kmpsdk.presentation.mvi.MviEffect
import com.kmpsdk.presentation.mvi.MviViewModel
import kotlinx.coroutines.CoroutineScope

sealed interface ListingEffect : MviEffect

class ListingViewModel(
    scope: CoroutineScope,
    repository: ListingRepository,
) : MviViewModel<ListingState, ListingIntent, ListingEffect>(
    initialState = ListingState(),
    reducer = ListingReducer,
    scope = scope,
) {
    private val listController = PaginatedListController(
        scope = scope,
        repository = repository,
        onStateChange = { paginatedState ->
            setState { current -> current.copy(listings = paginatedState) }
        },
        pageSize = ListingQuery.DEFAULT_PAGE_SIZE,
    )

    init {
        listController.start()
    }

    override fun dispatch(intent: ListingIntent) {
        super.dispatch(intent)
        when (intent) {
            ListingIntent.Refresh -> listController.refresh()
            ListingIntent.LoadMore -> listController.loadMore()
        }
    }
}

/** Factory for Android/iOS host apps. Resolves dependencies from the SDK registry. */
fun createListingViewModel(scope: CoroutineScope): ListingViewModel = ListingViewModel(
    scope = scope,
    repository = KmpSdk.get(),
)
