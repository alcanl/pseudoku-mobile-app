package com.alcanl.sudoku.global.extension

import android.widget.LinearLayout
import android.widget.ToggleButton
import androidx.core.view.children
import androidx.core.view.get

fun LinearLayout.refreshLayout()
{
    this.children.forEach { (it as ToggleButton).showButton() }
}

fun LinearLayout.hideCompletedNumber(value: Int)
{
    (this.children.elementAt(value - 1) as ToggleButton).hideButton()
}

fun LinearLayout.getSelectedToggleButton(value: Int) : ToggleButton
{
    return this[value - 1] as ToggleButton
}