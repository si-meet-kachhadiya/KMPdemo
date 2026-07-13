package com.kmpdemo

import android.app.Application
import com.kmpdemo.mvvm.configureMvvmDemo
import com.kmpsdk.KmpSdk
import com.kmpsdk.init

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KmpSdk.init(this) {
            configureMvvmDemo()
        }
    }
}
