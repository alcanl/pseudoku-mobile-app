package com.alcanl.sudoku

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
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
    private var mIsAnySelectedTextView = false
    private var mSelectedTextView : TextView? = null
    private var mSelectedToggleButton : ToggleButton? = null
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
        if (view.isSelected)
            runOnUiThread { tableCellClickedSelectedCallback(view) }
        else if (mIsAnySelectedTextView)
            return
        else
            runOnUiThread { tableCellClickedNotSelectedCallback(view) }
    }
    private fun tableCellClickedSelectedCallback(view: TextView)
    {
        val drawable = view.background.current as LayerDrawable
        (drawable.findDrawableByLayerId(R.id.textViewColor) as GradientDrawable).color = AppCompatResources.getColorStateList(this, com.androidplot.R.color.ap_white)
        view.setTextColor(AppCompatResources.getColorStateList(this, com.androidplot.R.color.ap_black))
        view.isSelected = false
        mIsAnySelectedTextView = false
        mSelectedTextView = null
    }
    private fun tableCellClickedNotSelectedCallback(view: TextView)
    {
        mSelectedTextView = view
        val drawable = view.background.current as LayerDrawable
        (drawable.findDrawableByLayerId(R.id.textViewColor) as GradientDrawable).color = AppCompatResources.getColorStateList(this, R.color.aqua)
        view.setTextColor(AppCompatResources.getColorStateList(this, R.color.white))
        mIsAnySelectedTextView = true
        view.isSelected = true

        if (mSelectedToggleButton != null && mSelectedToggleButton!!.isSelected) {
            val text = mSelectedToggleButton?.text?.toString()?.toInt()
            view.text = text.toString()
        }
    }
    private fun evaluateTheMove(view: TextView)
    {

    }
    fun toggleButtonClicked(toggleButton: ToggleButton)
    {
        mSelectedToggleButton = toggleButton

        if (mSelectedTextView != null)
            mSelectedTextView!!.text = toggleButton.text
    }
    fun buttonNewGameClicked()
    {
        mBinding.matrix!!.generateNewMatrix()
    }
}