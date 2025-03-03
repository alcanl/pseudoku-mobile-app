package com.alcanl.sudoku.global.extension

import android.view.View
import android.widget.ToggleButton

fun ToggleButton.hideButton()
{
    this.visibility = View.INVISIBLE
}

fun ToggleButton.showButton()
{
    this.visibility = View.VISIBLE
}