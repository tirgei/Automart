package com.gelostech.automartadmin.commoners

import android.support.multidex.MultiDexApplication
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class AutomartAdmin : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)
    }
}