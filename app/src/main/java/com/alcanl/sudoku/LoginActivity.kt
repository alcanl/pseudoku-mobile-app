package com.alcanl.sudoku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityLoginBinding
import com.alcanl.sudoku.service.SudokuApplicationDataService
import com.alcanl.sudoku.viewmodel.LoginActivityListenersViewModel
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var applicationService: SudokuApplicationDataService
    @Inject
    lateinit var threadPool: ExecutorService
    private lateinit var mBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        initialize()
    }
    private fun initialize()
    {
        initBinding()
    }
    private fun initBinding()
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.viewModel = LoginActivityListenersViewModel(this)
    }
    fun loginButtonClicked()
    {

    }
    fun signUpButtonClicked()
    {

    }
    fun googleAuthenticationButtonClicked()
    {

    }
    fun facebookAuthenticationButtonClicked()
    {

    }
}