package com.kmpdemo.mvi.presentation

import com.kmpdemo.mvi.model.ListingItem
import com.kmpsdk.domain.error.KmpSdkError
import com.kmpsdk.presentation.mvi.MviIntent

/**
 * MVI intents:
 * - User events: [Load], [Refresh]
 * - Result events (from ViewModel after side effects): [LoadStarted], [LoadSucceeded], [LoadFailed]
 *
 * The [ListingReducer] owns all state changes from these intents.
 */
sealed interface ListingIntent : MviIntent {
    /** Initial screen load. */
    data object Load : ListingIntent

    /** User-triggered reload. */
    data object Refresh : ListingIntent

    /** Side-effect started — show loading UI. */
    data object LoadStarted : ListingIntent

    /** API returned listing items. */
    data class LoadSucceeded(val items: List<ListingItem>) : ListingIntent

    /** API failed or device is offline. */
    data class LoadFailed(
        val error: KmpSdkError? = null,
        val offline: Boolean = false,
    ) : ListingIntent
}
