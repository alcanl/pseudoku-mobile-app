package com.alcanl.sudoku.viewmodel

import android.view.View
import com.alcanl.sudoku.MainActivity
import com.google.android.material.button.MaterialButton
import java.lang.ref.WeakReference

class SettingsListenersViewModel(activity: MainActivity) {
    private val mWeakReference = WeakReference(activity)

    fun handleButtonThemeSelect(view: View)
    {
        mWeakReference.get()!!.buttonSetThemeClicked(view as MaterialButton)
    }
}