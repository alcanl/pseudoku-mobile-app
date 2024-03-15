package com.alcanl.sudoku.global

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.alcanl.android.app.sudoku.R

const val TURKISH = "tr"
const val EASY_TR = "Kolay"
const val MEDIUM_TR = "Orta"
const val HARD_TR = "Zor"
const val EASY_LEVEL_COUNT = 37
const val MEDIUM_LEVEL_COUNT = 32
const val HARD_LEVEL_COUNT = 27

fun Array<IntArray>.transpose() : Array<IntArray> {

    val t = Array(this.size) { IntArray(this[0].size) }

    for (i in this.indices)
        for (j in this[i].indices)
            t[j][i] = this[i][j]

    return t
}

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
