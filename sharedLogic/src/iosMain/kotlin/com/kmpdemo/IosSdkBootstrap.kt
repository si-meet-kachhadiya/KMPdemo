package com.kmpdemo

import com.kmpdemo.mvvm.configureMvvmDemo
import com.kmpsdk.KmpSdk

/** Call once from iOS app startup before using shared APIs. */
fun bootstrapKmpSdkForIos() {
    KmpSdk.init {
        configureMvvmDemo()
    }
}
