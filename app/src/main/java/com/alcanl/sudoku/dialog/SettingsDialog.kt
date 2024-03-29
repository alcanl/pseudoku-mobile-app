package com.alcanl.sudoku.dialog

import android.app.Dialog
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.DialogSettingsLayoutBinding
import com.alcanl.sudoku.MainActivity
import com.alcanl.sudoku.viewmodel.SettingsListenersViewModel
import javax.inject.Inject

class SettingsDialog @Inject constructor(activity: MainActivity) : Dialog(activity, R.style.DialogStyle) {
    init {
        window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        setTitle("Select Theme")
        val binding: DialogSettingsLayoutBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.dialog_settings_layout, null, false)
        binding.viewModelDialog = SettingsListenersViewModel(activity)
        setContentView(binding.root)
    }
}