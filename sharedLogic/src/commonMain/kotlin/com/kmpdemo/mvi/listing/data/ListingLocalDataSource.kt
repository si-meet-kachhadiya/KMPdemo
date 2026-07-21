package com.kmpdemo.mvi.listing.data

import com.kmpdemo.mvi.listing.model.ListingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** Simple in-memory cache for listing (optional / demo). */
class ListingLocalDataSource {
    private val mutex = Mutex()
    private val items = MutableStateFlow<List<ListingItem>>(emptyList())

    fun observeAll(): Flow<List<ListingItem>> = items.asStateFlow()

    suspend fun count(): Long = mutex.withLock { items.value.size.toLong() }

    suspend fun replaceAll(newItems: List<ListingItem>) {
        mutex.withLock { items.value = newItems }
    }

    suspend fun appendAll(newItems: List<ListingItem>) {
        mutex.withLock { items.value = items.value + newItems }
    }
}
