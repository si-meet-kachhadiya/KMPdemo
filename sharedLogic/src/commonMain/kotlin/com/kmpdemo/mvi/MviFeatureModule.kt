package com.kmpdemo.mvi

import com.kmpdemo.mvi.detail.GetPhotoDetailUseCase
import com.kmpdemo.mvi.listing.GetListingsUseCase
import com.kmpsdk.core.di.KmpSdkModule
import com.kmpsdk.core.di.KmpSdkRegistry

object MviFeatureModule : KmpSdkModule {
    override fun register(registry: KmpSdkRegistry) {
        registry.register<GetListingsUseCase> { ctx ->
            GetListingsUseCase(ctx.networkClient)
        }
        registry.register<GetPhotoDetailUseCase> { ctx ->
            GetPhotoDetailUseCase(ctx.networkClient)
        }
    }
}
