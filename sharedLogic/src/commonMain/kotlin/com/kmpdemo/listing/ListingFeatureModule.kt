package com.kmpdemo.listing

import com.kmpsdk.core.di.KmpSdkModule
import com.kmpsdk.core.di.KmpSdkRegistry

/** Registers listing dependencies into the SDK registry. */
object ListingFeatureModule : KmpSdkModule {
    override fun register(registry: KmpSdkRegistry) {
        registry.register<ListingLocalDataSource> { ListingLocalDataSource() }
        registry.register<ListingRepository> { ctx ->
            ListingRepository(
                localDataSource = registry.resolve<ListingLocalDataSource>(),
                apiService = ListingApiService(ctx.networkClient),
                ctx = ctx,
            )
        }
    }
}
