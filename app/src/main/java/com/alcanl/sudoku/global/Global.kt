package com.alcanl.sudoku.global

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.get
import androidx.core.view.size
import com.alcanl.android.app.sudoku.R
import com.alcanl.sudoku.MainActivity
import com.alcanl.sudoku.global.theme.BoardTheme
import com.alcanl.sudoku.global.theme.BoardTheme.*

const val TURKISH = "tr"
const val EASY_TR = "Kolay"
const val MEDIUM_TR = "Orta"
const val HARD_TR = "Zor"
const val EASY_LEVEL_COUNT = 37
const val MEDIUM_LEVEL_COUNT = 32
const val HARD_LEVEL_COUNT = 27
const val WHAT_INVALID_USER_DATA = 1
const val WHAT_EMPTY_USER_DATA = 2
const val WHAT_ALREADY_TAKEN_USERNAME_OR_EMAIL = 3
const val WHAT_SERVICE_EX = 4
const val WHAT_SHOW_LOADING = 5
const val WHAT_HIDE_LOADING = 6

private fun FrameLayout.setDefaultColor(context: Context, backgroundColor: Int =  com.androidplot.R.color.ap_white, textColor: Int = com.androidplot.R.color.ap_black)
{
    val drawable = this.background.current as LayerDrawable
    (drawable.findDrawableByLayerId(R.id.textViewColor) as GradientDrawable).color =
        AppCompatResources.getColorStateList(context, backgroundColor)
    (this[0] as TextView).setTextColor(AppCompatResources.getColorStateList(context, textColor))
}
private fun FrameLayout.setLightColor(context: Context, backgroundColor: Int =  R.color.colorLightThemeBackground, textColor: Int = com.androidplot.R.color.ap_white)
{
    val drawable = this.background.current as LayerDrawable
    (drawable.findDrawableByLayerId(R.id.textViewColor) as GradientDrawable).color =
        AppCompatResources.getColorStateList(context, backgroundColor)
    (this[0] as TextView).setTextColor(AppCompatResources.getColorStateList(context, textColor))
}
private fun FrameLayout.setDarkColor(context: Context, backgroundColor: Int =  R.color.colorBlacky, textColor: Int = com.androidplot.R.color.ap_white)
{
    val drawable = this.background.current as LayerDrawable
    (drawable.findDrawableByLayerId(R.id.textViewColor) as GradientDrawable).color =
        AppCompatResources.getColorStateList(context, backgroundColor)
    (this[0] as TextView).setTextColor(AppCompatResources.getColorStateList(context, textColor))
}
private fun TextView.updateTextColor(context: Context, theme: BoardTheme)
{
    this.setTextColor(when (theme) {
        THEME_DARK -> context.getColor(com.androidplot.R.color.ap_white)
        THEME_DEFAULT -> context.getColor(com.androidplot.R.color.ap_black)
        else -> context.getColor(com.androidplot.R.color.ap_white)
    })
}
private fun FrameLayout.setDrawableLeftAndTop(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_left_top_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_left_top_light
            else -> R.drawable.textview_layout_border_left_top
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
private fun FrameLayout.setDrawableLeft(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_left_only_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_left_only_light
            else -> R.drawable.textview_layout_border_left_only
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
private fun FrameLayout.setDrawableLeftAndBottom(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_left_bottom_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_left_bottom_light
            else -> R.drawable.textview_layout_border_left_bottom
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
private fun FrameLayout.setDrawableRightAndTop(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_right_top_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_right_top_light
            else -> R.drawable.textview_layout_border_right_top
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
private fun FrameLayout.setDrawableRight(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_right_only_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_right_only_light
            else -> R.drawable.textview_layout_border_right_only
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
private fun FrameLayout.setDrawableRightAndBottom(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_right_bottom_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_right_bottom_light
            else -> R.drawable.textview_layout_border_right_bottom
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
private fun FrameLayout.setDrawableBottom(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_bottom_only_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_bottom_only_light
            else -> R.drawable.textview_layout_border_bottom_only
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
private fun FrameLayout.setDrawableTop(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_top_only_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_top_only_light
            else -> R.drawable.textview_layout_border_top_only
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
private fun FrameLayout.setDrawableNot(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_not_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_not_light
            else -> R.drawable.textview_layout_border_not
        })
    (this[0] as TextView).updateTextColor(context, theme)
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
fun FrameLayout.getMoveInfo(toggleButton: ToggleButton) : Pair<Int, String>
{
    val index = resources.getResourceEntryName(this.id).substring(11).toInt()
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
fun FrameLayout.setLineColor(context: Context)
{
    when ((context as MainActivity).gameInfo.activeTheme()) {
        THEME_DARK -> this.setDarkColor(context, R.color.colorDarkThemeLine)
        THEME_LIGHT -> this.setLightColor(context, R.color.colorLightThemeLine)
        THEME_DEFAULT -> this.setDefaultColor(context, R.color.line_color)
    }
}
fun FrameLayout.setSelectedColor(context: Context)
{
    when ((context as MainActivity).gameInfo.activeTheme()) {
        THEME_DARK -> this.setDarkColor(context, R.color.colorDarkThemeSelected)
        THEME_LIGHT -> this.setLightColor(context, R.color.colorLightThemeSelected)
        THEME_DEFAULT -> this.setDefaultColor(context, R.color.aqua)
    }
}
fun FrameLayout.clearColor(context: Context)
{
    when ((context as MainActivity).gameInfo.activeTheme()) {
        THEME_DARK -> this.setDarkColor(context)
        THEME_LIGHT -> this.setLightColor(context)
        THEME_DEFAULT -> this.setDefaultColor(context)
    }
}
fun TableLayout.setTheme(context: Context, theme: BoardTheme)
{
    for (i in 0..< this.size) {
        val tableRow = this[i] as TableRow
        for (k in 0..< tableRow.size) {
            val frameLayout = tableRow[k] as FrameLayout
            if (i % 3 == 0) {
                if (k % 3 == 0)
                    frameLayout.setDrawableLeftAndTop(context, theme)
                else if (k % 3 == 2)
                    frameLayout.setDrawableRightAndTop(context, theme)
                else
                    frameLayout.setDrawableTop(context, theme)
            }
            else if (i % 3 == 2) {
                if (k % 3 == 0)
                    frameLayout.setDrawableLeftAndBottom(context, theme)
                else if (k % 3 == 2)
                    frameLayout.setDrawableRightAndBottom(context, theme)
                else
                    frameLayout.setDrawableBottom(context, theme)
            }
            else
                if (k % 3 == 0)
                    frameLayout.setDrawableLeft(context, theme)
                else if (k % 3 == 2)
                    frameLayout.setDrawableRight(context, theme)
                else
                    frameLayout.setDrawableNot(context, theme)
        }
    }
}

