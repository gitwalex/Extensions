package com.gerwalex.extensions

import android.content.res.Resources

object FloatExt {

    /**
     * Umrechnung Pixel in DP
     */
    @JvmStatic
    fun Float.pxToDP(): Float = (this / Resources.getSystem().displayMetrics.density)

    /**
     * Umrechnung DP in Pixel
     */
    @JvmStatic
    fun Float.dpToPx(): Float = (this * Resources.getSystem().displayMetrics.density)

    /**
     * Umrechnung Pixel in SP
     */
    @JvmStatic
    fun Float.pxToSp(): Float = (this / Resources.getSystem().displayMetrics.scaledDensity)

    /**
     * Umrechnung SP in Pixel
     */
    @JvmStatic
    fun Float.spToPx(): Float = (this * Resources.getSystem().displayMetrics.scaledDensity)
}