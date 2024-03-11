package com.alcanl.sudoku.viewmodel

import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import com.alcanl.sudoku.MainActivity
import java.lang.ref.WeakReference

class MainActivityListenersViewModel(activity: MainActivity) {
    private val mWeakReference = WeakReference(activity)
    fun handleTableCellClick(view: View)
    {
        mWeakReference.get()?.tableCellClicked(view as TextView)
    }
    fun handleToggleButtonClick(view: View)
    {
        mWeakReference.get()?.toggleButtonClicked(view as ToggleButton)
    }
}