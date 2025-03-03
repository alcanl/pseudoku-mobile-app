package com.alcanl.sudoku.global.extension

import android.content.Context
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.size
import com.alcanl.sudoku.global.theme.BoardTheme

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

fun TableLayout.clearTableBackground(context: Context)
{
    this.children.forEach { view ->
        (view as TableRow).forEach {
            (it as FrameLayout).clearColor(context)
        }
    }
}

fun TableLayout.getSelectedFrame(index: Int) : FrameLayout
{
    return (this[index / 10] as TableRow)[index % 10] as FrameLayout
}

fun TableLayout.setLineBackGround(index: Int, context: Context)
{
    var tableRow: TableRow
    var frameLayout: FrameLayout
    for (i in 0..< this.size) {
        tableRow = this[i] as TableRow
        for (k in 0..<tableRow.size) {
            frameLayout = tableRow[k] as FrameLayout
            if (index == i * 10 + k)
                frameLayout.setSelectedColor(context)
            else if (index % 10 == k || index / 10 == i)
                frameLayout.setLineColor(context)
            else
                frameLayout.clearColor(context)
        }
    }
}