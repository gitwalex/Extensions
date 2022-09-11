package com.gerwalex.extensions

import android.content.res.Resources

object IntExt {

    /**
     * Umrechnung Pixel in DP
     */
    @JvmStatic
    fun Int.pxToDP(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

    /**
     * Umrechnung DP in Pixel
     */
    @JvmStatic
    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    /**
     * Umrechnung Pixel in SP
     */
    @JvmStatic
    fun Int.pxToSp(): Int = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()

    /**
     * Umrechnung Sp in Pixel
     */
    @JvmStatic
    fun Int.spToPx(): Int = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()
}