package com.kmpdemo.mvvm

import com.kmpsdk.KmpSdkInitBuilder
import com.kmpsdk.core.config.SdkProfile
import com.kmpsdk.core.logger.LogLevel
import com.kmpsdk.domain.sync.SyncPolicy

/** SDK setup for the MVVM listing demo (KKR API). */
fun KmpSdkInitBuilder.configureMvvmDemo() {
    profile = SdkProfile.DEVELOPMENT
    baseUrl = "https://apps.kkr.in"
    logLevel = LogLevel.DEBUG
    enableRequestLogging = true
    enableResponseBodyLogging = true
    syncPolicy = SyncPolicy.NETWORK_FIRST
    enableHttpCache = true
    autoSyncOnReconnect = false
    queueMutationsWhenOffline = false
    install(MvvmFeatureModule)
}
