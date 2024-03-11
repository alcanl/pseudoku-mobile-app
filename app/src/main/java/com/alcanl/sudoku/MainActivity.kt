package com.alcanl.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityMainBinding
import com.alcanl.sudoku.entity.SudokuMatrix
import com.alcanl.sudoku.entity.gameplay.GamePlay
import com.alcanl.sudoku.timer.TimeCounter
import com.alcanl.sudoku.viewmodel.MainActivityListenersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var threadPool: ExecutorService
    @Inject
    lateinit var scheduledThreadPool: ScheduledExecutorService
    @Inject
    lateinit var timeCounter: TimeCounter
    @Inject
    lateinit var mSudokuMatrix: SudokuMatrix
    @Inject
    lateinit var mGamePlay: GamePlay
    private lateinit var mBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        initialize()
    }
    private fun initialize()
    {
        initBinding()
        initCounter()
    }
    private fun initCounter()
    {
        scheduledThreadPool.scheduleAtFixedRate({ counterCallback() }, 0, 1, TimeUnit.SECONDS)
    }
    @Synchronized
    private fun counterCallback()
    {
        timeCounter.handleCounter()
        mBinding.timer = timeCounter.toString()
    }
    private fun initBinding()
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = MainActivityListenersViewModel(this)
        mBinding.matrix = mSudokuMatrix
    }
    fun tableCellClicked(view: TextView)
    {

    }
    fun toggleButtonClicked(toggleButton: ToggleButton)
    {

    }
    fun buttonNewGameClicked()
    {
        mBinding.matrix!!.generateNewMatrix()
    }
}