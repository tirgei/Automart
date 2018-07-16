package com.gelostech.automart.callbacks

import android.view.View
import com.gelostech.automart.models.Car

interface CarCallback {

    fun onClick(v: View, car: Car)

}