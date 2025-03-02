package com.alcanl.sudoku

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        initCountDownTimer()
    }

    private fun initCountDownTimer()
    {
        object : CountDownTimer(4000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                Intent(this@SplashActivity, LoginActivity::class.java)
                    .apply { finish(); startActivity(this) }
            }
        }.start()
    }
}