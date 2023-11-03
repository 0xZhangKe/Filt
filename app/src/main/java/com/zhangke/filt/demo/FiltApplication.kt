package com.zhangke.filt.demo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FiltApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}