package com.alcanl.sudoku.viewmodel

import com.alcanl.sudoku.MainActivity
import java.lang.ref.WeakReference

class MainActivityListenersViewModel(activity: MainActivity) {
    private val mWeakReference = WeakReference(activity)
    fun handleTableCellClick()
    {
        mWeakReference.get()?.tableCellClicked()
    }
}