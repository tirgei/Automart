package com.gelostech.automart.commoners

import android.support.multidex.MultiDexApplication
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class Automart : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return
//        }
//
//        LeakCanary.install(this)
    }
}