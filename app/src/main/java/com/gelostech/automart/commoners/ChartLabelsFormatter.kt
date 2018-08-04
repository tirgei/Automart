package com.gelostech.automart.commoners

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import timber.log.Timber

class ChartLabelsFormatter(val labels: Array<String>) : IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        Timber.e("Labels size: ${labels.size}")
        Timber.e("Value: ${value.toInt()}")

        if (value.toInt() < labels.size) {
            return labels[(value.toInt())]
        } else {
            return ""
        }
    }
}