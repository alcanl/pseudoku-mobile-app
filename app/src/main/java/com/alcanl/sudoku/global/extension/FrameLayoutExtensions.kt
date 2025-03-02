package com.alcanl.sudoku.global.extension

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.get
import com.alcanl.android.app.sudoku.R
import com.alcanl.sudoku.MainActivity
import com.alcanl.sudoku.global.theme.BoardTheme
import com.alcanl.sudoku.global.theme.BoardTheme.THEME_DARK
import com.alcanl.sudoku.global.theme.BoardTheme.THEME_DEFAULT
import com.alcanl.sudoku.global.theme.BoardTheme.THEME_LIGHT

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

fun FrameLayout.setDrawableLeftAndTop(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_left_top_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_left_top_light
            else -> R.drawable.textview_layout_border_left_top
        })
    (this[0] as TextView).updateTextColor(context, theme)
}

fun FrameLayout.setDrawableLeft(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_left_only_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_left_only_light
            else -> R.drawable.textview_layout_border_left_only
        })
    (this[0] as TextView).updateTextColor(context, theme)
}

fun FrameLayout.setDrawableLeftAndBottom(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_left_bottom_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_left_bottom_light
            else -> R.drawable.textview_layout_border_left_bottom
        })
    (this[0] as TextView).updateTextColor(context, theme)
}

fun FrameLayout.setDrawableRightAndTop(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_right_top_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_right_top_light
            else -> R.drawable.textview_layout_border_right_top
        })
    (this[0] as TextView).updateTextColor(context, theme)
}

fun FrameLayout.setDrawableRight(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_right_only_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_right_only_light
            else -> R.drawable.textview_layout_border_right_only
        })
    (this[0] as TextView).updateTextColor(context, theme)
}

fun FrameLayout.setDrawableRightAndBottom(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_right_bottom_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_right_bottom_light
            else -> R.drawable.textview_layout_border_right_bottom
        })
    (this[0] as TextView).updateTextColor(context, theme)
}

fun FrameLayout.setDrawableBottom(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_bottom_only_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_bottom_only_light
            else -> R.drawable.textview_layout_border_bottom_only
        })
    (this[0] as TextView).updateTextColor(context, theme)
}

fun FrameLayout.setDrawableTop(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_top_only_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_top_only_light
            else -> R.drawable.textview_layout_border_top_only
        })
    (this[0] as TextView).updateTextColor(context, theme)
}
fun FrameLayout.setDrawableNot(context: Context, theme: BoardTheme)
{
    this.background = AppCompatResources.getDrawable(context,
        when (theme) {
            THEME_DARK -> R.drawable.textview_layout_border_not_dark
            THEME_LIGHT -> R.drawable.textview_layout_border_not_light
            else -> R.drawable.textview_layout_border_not
        })
    (this[0] as TextView).updateTextColor(context, theme)
}

fun FrameLayout.getMoveInfo(toggleButton: ToggleButton) : Pair<Int, String>
{
    val index = resources.getResourceEntryName(this.id).substring(11).toInt()
    val value = toggleButton.text.toString()

    return Pair(index, value)
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