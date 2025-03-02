package com.alcanl.sudoku.global.extension

import android.content.Context
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import com.alcanl.android.app.sudoku.R

fun ImageButton.setAnimation(context: Context, animationListener: AnimationListener)
{
    this.apply {
        startAnimation(
            AnimationUtils.loadAnimation(context,
                R.anim.alpha_blink_anim).also { it.setAnimationListener(animationListener) })
    }
}