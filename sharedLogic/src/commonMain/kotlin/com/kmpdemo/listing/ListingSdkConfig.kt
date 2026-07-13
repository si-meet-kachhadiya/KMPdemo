package com.kmpdemo.listing.presentation

import com.kmpdemo.listing.ListingFeatureModule
import com.kmpsdk.KmpSdkInitBuilder
import com.kmpsdk.core.config.SdkProfile
import com.kmpsdk.core.logger.LogLevel
import com.kmpsdk.domain.sync.SyncPolicy

/** Shared SDK configuration used by Android and iOS. */
fun KmpSdkInitBuilder.configureListingDemo() {
    profile = SdkProfile.DEVELOPMENT
    baseUrl = "https://apps.kkr.in"
    logLevel = LogLevel.DEBUG
    enableRequestLogging = true
    enableResponseBodyLogging = false
    syncPolicy = SyncPolicy.NETWORK_FIRST
    enableHttpCache = true
    autoSyncOnReconnect = false
    queueMutationsWhenOffline = false
    install(ListingFeatureModule)
}
