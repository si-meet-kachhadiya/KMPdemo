package com.kmpdemo.mvi

import com.kmpdemo.config.BuildConfig
import com.kmpsdk.KmpSdkInitBuilder
import com.kmpsdk.core.config.SdkProfile
import com.kmpsdk.core.logger.LogLevel
import com.kmpsdk.domain.sync.SyncPolicy

/** SDK setup for the MVI listing demo — base URL / profile from [BuildConfig]. */
fun KmpSdkInitBuilder.configureMviDemo() {
    profile = when {
        BuildConfig.isProduction -> SdkProfile.PRODUCTION
        BuildConfig.isStaging -> SdkProfile.STAGING
        else -> SdkProfile.DEVELOPMENT
    }
    baseUrl = BuildConfig.apiBaseUrl
    logLevel = if (BuildConfig.isDebug) LogLevel.DEBUG else LogLevel.INFO
    enableRequestLogging = BuildConfig.isDebug
    enableResponseBodyLogging = BuildConfig.isDebug
    syncPolicy = SyncPolicy.NETWORK_FIRST
    enableHttpCache = true
    autoSyncOnReconnect = false
    queueMutationsWhenOffline = false
    install(MviFeatureModule)
}
