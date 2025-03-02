package com.alcanl.sudoku.global.extension

import android.content.Context
import android.widget.TextView
import com.alcanl.sudoku.global.theme.BoardTheme
import com.alcanl.sudoku.global.theme.BoardTheme.THEME_DARK
import com.alcanl.sudoku.global.theme.BoardTheme.THEME_DEFAULT

fun TextView.updateTextColor(context: Context, theme: BoardTheme)
{
    this.setTextColor(when (theme) {
        THEME_DARK -> context.getColor(com.androidplot.R.color.ap_white)
        THEME_DEFAULT -> context.getColor(com.androidplot.R.color.ap_black)
        else -> context.getColor(com.androidplot.R.color.ap_white)
    })
}
