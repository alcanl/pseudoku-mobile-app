package com.alcanl.sudoku.viewmodel

import android.view.View
import android.widget.FrameLayout
import android.widget.ToggleButton
import com.alcanl.sudoku.MainActivity
import java.lang.ref.WeakReference

class MainActivityListenersViewModel(activity: MainActivity) {
    private val mWeakReference = WeakReference(activity)
    fun handleTableCellClick(view: View)
    {
        mWeakReference.get()?.tableCellClicked(view as FrameLayout)
    }
    fun handleToggleButtonClick(view: View)
    {
        mWeakReference.get()?.toggleButtonClicked(view as ToggleButton)
    }
    fun handleHintButtonClick()
    {
        mWeakReference.get()?.buttonHintClicked()
    }
    fun handleBackButtonClick()
    {
        mWeakReference.get()?.buttonBackClicked()
    }
    fun handleSettingsButtonClick()
    {
        mWeakReference.get()?.buttonSettingsClicked()
    }
    fun handleUndoButtonClick()
    {
        mWeakReference.get()?.buttonUndoClicked()
    }
    fun handleRestartButtonClick()
    {
        mWeakReference.get()?.buttonRestartClicked()
    }
    fun handleNoteButtonClick()
    {
        mWeakReference.get()?.buttonNoteClicked()
    }
    fun handleUserButtonClick()
    {
        mWeakReference.get()?.buttonUserClicked()
    }
}