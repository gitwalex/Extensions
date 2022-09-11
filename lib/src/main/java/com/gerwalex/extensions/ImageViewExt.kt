package com.gerwalex.extensions

import android.content.Context
import android.graphics.Bitmap
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

object ImageViewExt {

    fun ImageView.animatedChange(c: Context, new_image: Bitmap?) {
        val anim_out: Animation = AnimationUtils.loadAnimation(c, android.R.anim.fade_out)
        val anim_in: Animation = AnimationUtils.loadAnimation(c, android.R.anim.fade_in)
        anim_out.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                setImageBitmap(new_image)
                anim_in.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {}
                })
                startAnimation(anim_in)
            }
        })
        startAnimation(anim_out)
    }
}