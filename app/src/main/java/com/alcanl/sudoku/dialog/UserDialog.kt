package com.alcanl.sudoku.dialog

import android.app.Dialog
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.LayoutDialogUserBinding
import com.alcanl.sudoku.MainActivity
import com.alcanl.sudoku.viewmodel.UserDialogListenersViewModel
import javax.inject.Inject

class UserDialog @Inject constructor(activity: MainActivity) : Dialog(activity, R.style.DialogStyle) {
    init {
        window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        setTitle("User Info")
        setCancelable(true)
        val binding: LayoutDialogUserBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.dialog_settings_layout, null, false)
        binding.viewModelDialog = UserDialogListenersViewModel(activity)
        setContentView(binding.root)
    }

}