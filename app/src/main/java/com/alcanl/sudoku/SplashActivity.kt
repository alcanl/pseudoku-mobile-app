package com.alcanl.sudoku

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivitySplashBinding
import com.alcanl.sudoku.repository.dal.SudokuApplicationHelper
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo
import com.alcanl.sudoku.service.SudokuApplicationDataService
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivitySplashBinding
    @Inject
    lateinit var service: SudokuApplicationDataService
    @Inject
    lateinit var threadPool: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        initCountDownTimer()
    }
    private fun initCountDownTimer()
    {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(p0: Long) {
                Log.e("test", "test")
            }

            override fun onFinish() {
                Intent(this@SplashActivity, MainActivity::class.java)
                    .apply { finish(); startActivity(this) }
            }
        }.start()
    }
}