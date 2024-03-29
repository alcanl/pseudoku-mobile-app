package com.alcanl.sudoku.viewmodel

import com.alcanl.sudoku.MainActivity
import java.lang.ref.WeakReference

class UserDialogListenersViewModel(activity: MainActivity) {
    private val mWeakReference = WeakReference(activity)
}