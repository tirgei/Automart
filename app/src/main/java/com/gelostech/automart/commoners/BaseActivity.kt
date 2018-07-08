package com.gelostech.automart.commoners

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gelostech.automart.utils.Connectivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun isConnected(): Boolean {
        return Connectivity.isConnected(this)
    }
}
