package com.kmpdemo

import com.kmpdemo.mvi.configureMviDemo
import com.kmpsdk.KmpSdk

/** Call once from iOS app startup before using shared APIs. */
fun bootstrapKmpSdkForIos() {
    KmpSdk.init {
        configureMviDemo()
    }
}
