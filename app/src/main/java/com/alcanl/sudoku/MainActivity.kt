package com.alcanl.sudoku

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityMainBinding
import com.alcanl.sudoku.entity.SudokuMatrix
import com.alcanl.sudoku.entity.gameplay.GamePlay
import com.alcanl.sudoku.timer.ChronometerCounter
import com.alcanl.sudoku.viewmodel.MainActivityListenersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var threadPool: ExecutorService
    @Inject
    lateinit var scheduledThreadPool: ScheduledExecutorService
    @Inject
    lateinit var chronometerCounter: ChronometerCounter
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
    private fun counterCallback()
    {
        chronometerCounter.handleCounter()
        mBinding.timer = chronometerCounter.toString()
    }
    private fun initBinding()
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = MainActivityListenersViewModel(this)
        mBinding.matrix = mSudokuMatrix
    }
    private fun tableCellClickedSelectedCallback(view: TextView)
    {
        setTextViewColor(view, com.androidplot.R.color.ap_white, com.androidplot.R.color.ap_black)
        view.isSelected = false
        mIsAnySelectedTextView = false
        mSelectedTextView = null
    }
    private fun tableCellClickedNotSelectedCallback(view: TextView)
    {
        mSelectedTextView = view
        setTextViewColor(view, R.color.aqua, com.androidplot.R.color.ap_white)
        mIsAnySelectedTextView = true
        view.isSelected = true

        if (mSelectedToggleButton != null && mSelectedToggleButton!!.isSelected) {
            view.text  = mSelectedToggleButton?.text?.toString()
            evaluateTheMove(view, mSelectedToggleButton!!)
        }
    }
    private fun setTextViewColor(view: TextView, backgroundColor: Int, textColor: Int)
    {
        val drawable = view.background.current as LayerDrawable
        (drawable.findDrawableByLayerId(R.id.textViewColor) as GradientDrawable).color = AppCompatResources.getColorStateList(this, backgroundColor)
        view.setTextColor(AppCompatResources.getColorStateList(this, textColor))
    }
    @Synchronized
    private fun trueMoveCallback(view: TextView)
    {
        setTextViewColor(view, R.color.trueMove, com.androidplot.R.color.ap_white)

        val value = mSelectedToggleButton!!.text.toString().toInt()
        view.isClickable = false
        clearSelects()

        mSudokuMatrix.decreaseNumberCount(value)
        if (mSudokuMatrix.getNumberCounts()[value - 1] == 0)
            mBinding.linearLayoutButtons.children.elementAt(value - 1).visibility = View.INVISIBLE

        Handler(Looper.myLooper()!!).postDelayed({setTextViewColor(view, com.androidplot.R.color.ap_white, com.androidplot.R.color.ap_black)}, 2000)
    }
    private fun clearSelects()
    {
        mIsAnySelectedTextView = false
        mSelectedTextView = null
        mSelectedToggleButton = null
    }
    @Synchronized
    private fun falseMoveCallback(view: TextView)
    {
        setTextViewColor(view, R.color.falseMove, com.androidplot.R.color.ap_white)
        clearSelects()
        Handler(Looper.myLooper()!!).postDelayed(
            {setTextViewColor(view,
                com.androidplot.R.color.ap_white, com.androidplot.R.color.ap_black)
                mGamePlay.apply {
                    if (checkIfExistErrorCount()) {
                        errorDone()
                        setTextViewColor(view, com.androidplot.R.color.ap_white, com.androidplot.R.color.ap_black)
                        view.text = ""
                    }
                    else
                        stopGame()
                }
            }, 2000)
    }
    private fun evaluateTheMove(view: TextView, toggleButton: ToggleButton)
    {
        val value = toggleButton.text.toString().toInt()
        val index = resources.getResourceEntryName(view.id).substring(8).toInt()
        if (mSudokuMatrix.isTrueValue(value, index))
            runOnUiThread { trueMoveCallback(mSelectedTextView!!) }
        else
            runOnUiThread { falseMoveCallback(mSelectedTextView!!) }
    }
    private fun stopGame()
    {

    }
    fun toggleButtonClicked(toggleButton: ToggleButton)
    {
        mSelectedToggleButton = toggleButton

        if (mSelectedTextView != null) {
            mSelectedTextView!!.text = toggleButton.text
            evaluateTheMove(mSelectedTextView!!, toggleButton)
        }
    }
    fun buttonNewGameClicked()
    {
        mBinding.matrix!!.generateNewMatrix()
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
    fun buttonBackClicked()
    {

    }
    fun buttonSettingsClicked()
    {

    }
    fun buttonUndoClicked()
    {

    }
    fun buttonNoteClicked()
    {

    }
    fun buttonHintClicked()
    {
        val random = Random
        val index = random.nextInt(1 , 9) * 10 + random.nextInt(1, 9)
        val value = mSudokuMatrix.getValue(index)

        if (value.isEmpty()) {
            buttonHintClicked()
            return
        }

        ((mBinding.tableLayoutMain[index / 10] as TableRow)[index % 10] as TextView).text = value
    }
    fun buttonUserClicked()
    {

    }
    fun buttonRestartClicked()
    {

    }
}