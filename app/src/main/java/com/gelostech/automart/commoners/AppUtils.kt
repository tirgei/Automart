package com.gelostech.automart.commoners

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.gelostech.automart.R
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon

object AppUtils {

    fun animateEnterRight(activity: Activity) {
        activity.overridePendingTransition(R.anim.enter_b, R.anim.exit_a)
    }

    fun animateEnterLeft(activity: Activity) {
        activity.overridePendingTransition(R.anim.enter_a, R.anim.exit_b)
    }

    fun animateFadein(activity: Activity) {
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * This function returns a FontAwesome drawable
     *
     * @param context Activity context
     * @param icon FontAwesome icon
     * @param color Color to set to icon
     * @param size Size of icon
     */
    fun setDrawable(context: Context, icon: IIcon, color: Int, size: Int): Drawable {
        return IconicsDrawable(context).icon(icon).color(ContextCompat.getColor(context, color)).sizeDp(size)
    }


    /**
     *  This function returns a drawable from a Bitmap
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun getColor(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    fun validated(vararg views: View): Boolean {
        var ok = true
        for (v in views) {
            if (v is EditText) {
                if (TextUtils.isEmpty(v.text.toString())) {
                    ok = false
                    v.error = "Required"
                }
            }
        }
        return ok
    }

}