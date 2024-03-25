package com.alcanl.sudoku.global

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
import com.alcanl.android.app.sudoku.R

const val TURKISH = "tr"
const val EASY_TR = "Kolay"
const val MEDIUM_TR = "Orta"
const val HARD_TR = "Zor"
const val EASY_LEVEL_COUNT = 37
const val MEDIUM_LEVEL_COUNT = 32
const val HARD_LEVEL_COUNT = 27


fun TextView.setColor(context: Context, backgroundColor: Int =  com.androidplot.R.color.ap_white, textColor: Int = com.androidplot.R.color.ap_black)
{
    val drawable = this.background.current as LayerDrawable
    (drawable.findDrawableByLayerId(R.id.textViewColor) as GradientDrawable).color = AppCompatResources.getColorStateList(context, backgroundColor)
    this.setTextColor(AppCompatResources.getColorStateList(context, textColor))
}
fun TextView.enableNoteMode(context: Context)
{
    this.textSize = 10f
    this.gravity = Gravity.BOTTOM
    this.setTextColor(context.getColor(com.androidplot.R.color.ap_gray))
    this.isSelected = true
}
fun TextView.disableNoteMode(context: Context)
{
    this.textSize = 30f
    this.gravity = Gravity.CENTER
    this.setTextColor(context.getColor(com.androidplot.R.color.ap_black))
    this.isSelected = false
}
fun TextView.getMoveInfo(toggleButton: ToggleButton) : Pair<Int, String>
{
    val index = resources.getResourceEntryName(this.id).substring(8).toInt()
    val value = toggleButton.text.toString()

    return Pair(index, value)
}
fun ImageButton.setAnimation(context: Context, animationListener: AnimationListener)
{
    this.apply {
        startAnimation(
            AnimationUtils.loadAnimation(context,
                R.anim.alpha_blink_anim).also { it.setAnimationListener(animationListener) })
    }
}
