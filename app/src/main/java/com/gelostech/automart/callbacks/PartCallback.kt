package com.gelostech.automart.callbacks

import android.view.View
import com.gelostech.automart.models.Part

interface PartCallback {

    fun onClick(v: View, part: Part)

}