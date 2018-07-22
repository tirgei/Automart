package com.gelostech.automart.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils

class AddPartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_part)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)
    }
}
