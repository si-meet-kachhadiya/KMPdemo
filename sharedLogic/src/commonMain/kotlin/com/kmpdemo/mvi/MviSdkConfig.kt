package com.kmpdemo.mvi

import com.kmpsdk.KmpSdkInitBuilder
import com.kmpsdk.core.config.SdkProfile
import com.kmpsdk.core.logger.LogLevel
import com.kmpsdk.domain.sync.SyncPolicy

/** SDK setup for the MVI listing demo (KKR API). */
fun KmpSdkInitBuilder.configureMviDemo() {
    profile = SdkProfile.DEVELOPMENT
    baseUrl = "https://apps.kkr.in"
    logLevel = LogLevel.DEBUG
    enableRequestLogging = true
    enableResponseBodyLogging = true
    syncPolicy = SyncPolicy.NETWORK_FIRST
    enableHttpCache = true
    autoSyncOnReconnect = false
    queueMutationsWhenOffline = false
    install(MviFeatureModule)
}
