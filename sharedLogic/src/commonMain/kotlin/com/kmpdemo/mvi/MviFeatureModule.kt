package com.kmpdemo.mvi

import com.kmpsdk.core.di.KmpSdkModule
import com.kmpsdk.core.di.KmpSdkRegistry

object MviFeatureModule : KmpSdkModule {
    override fun register(registry: KmpSdkRegistry) {
        registry.register<GetListingsUseCase> { ctx ->
            GetListingsUseCase(ctx.networkClient)
        }
    }
}
