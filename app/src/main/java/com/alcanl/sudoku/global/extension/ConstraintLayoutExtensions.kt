package com.alcanl.sudoku.global.extension

import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.alcanl.android.app.sudoku.R
import com.alcanl.sudoku.global.theme.BoardTheme

private fun ConstraintLayout.setFooterTheme(context: Context, color: Int)
{
    this.forEach { view ->
        if (view is FrameLayout)
            view.forEach {
                if (it is TextView && it.id != R.id.textViewHintCount)
                    it.setTextColor(ContextCompat.getColor(context, color))
            }
    }
}

private fun ConstraintLayout.setFooterDarkTheme(context: Context)
{
    setFooterTheme(context, com.androidplot.R.color.ap_white)
}

private fun ConstraintLayout.setFooterLightTheme(context: Context)
{
    setFooterTheme(context, com.androidplot.R.color.ap_black)
}

private fun ConstraintLayout.setDarkTheme(context: Context)
{
    this.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_dark_background))

    this.forEach {
        if (it is ConstraintLayout) {
            when (it.id) {
                R.id.constraintLayoutFooter -> {
                    it.setFooterDarkTheme(context)
                    it.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_dark_background))
                }
                R.id.constraintLayoutHeader ->
                    it.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_dark_background))
                else ->
                    it.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_dark_background))
            }
        }
    }
}

private fun ConstraintLayout.setLightTheme(context: Context)
{
    this.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_light_background))

    this.forEach {
        if (it is ConstraintLayout) {
            when (it.id) {
                R.id.constraintLayoutFooter -> {
                    it.setFooterLightTheme(context)
                    it.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_shadow_end_color))
                }
                R.id.constraintLayoutHeader ->
                    it.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_shadow_end_color))
                else ->
                    it.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_light_background))
            }
        }
    }
}

private fun ConstraintLayout.setColoredTheme(context: Context)
{
    TODO("Not implemented yet")
}

fun ConstraintLayout.setTheme(context: Context, theme: BoardTheme)
{
    when (theme) {
        BoardTheme.THEME_DARK -> this.setDarkTheme(context)
        BoardTheme.THEME_DEFAULT -> this.setLightTheme(context)
        else ->this.setColoredTheme(context)
    }
}
