package com.kmpdemo.mvvm

import com.kmpsdk.core.di.KmpSdkModule
import com.kmpsdk.core.di.KmpSdkRegistry

object MvvmFeatureModule : KmpSdkModule {
    override fun register(registry: KmpSdkRegistry) {
        registry.register<GetListingsUseCase> { ctx ->
            GetListingsUseCase(ctx.networkClient)
        }
    }
}
