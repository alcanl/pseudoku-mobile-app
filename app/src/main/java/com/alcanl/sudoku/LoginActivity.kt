package com.alcanl.sudoku

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityLoginBinding
import com.alcanl.sudoku.service.SudokuApplicationDataService
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
    }
}