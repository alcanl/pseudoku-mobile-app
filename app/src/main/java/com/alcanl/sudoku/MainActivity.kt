package com.alcanl.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityMainBinding
import com.alcanl.sudoku.entity.SudokuMatrix
import com.alcanl.sudoku.entity.User
import com.alcanl.sudoku.entity.gameplay.GamePlay
import com.alcanl.sudoku.global.setColor
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
    lateinit var sudokuMatrix: SudokuMatrix
    @Inject
    lateinit var gamePlay: GamePlay
    @Inject
    lateinit var user: User
    private lateinit var mBinding : ActivityMainBinding
    private var mIsAnySelectedTextView = false
    private var mSelectedTextView : TextView? = null
    private var mSelectedToggleButton : ToggleButton? = null

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
        mBinding.matrix = sudokuMatrix
        mBinding.gamePlay = gamePlay
        mBinding.user = user
    }
    private fun tableCellClickedSelectedCallback(view: TextView)
    {
        view.setColor(this)
        runOnUiThread { setLineBackground(resources.getResourceEntryName(view.id).substring(8).toInt()) }
        view.isSelected = false
        mIsAnySelectedTextView = false
        mSelectedTextView = null
    }
    private fun tableCellClickedNotSelectedCallback(view: TextView)
    {
        mSelectedTextView = view
        view.setColor(this, R.color.aqua, com.androidplot.R.color.ap_white)
        mIsAnySelectedTextView = true
        view.isSelected = true

        if (mSelectedToggleButton != null && mSelectedToggleButton!!.isSelected) {
            view.text  = mSelectedToggleButton?.text?.toString()
            evaluateTheMove(view, mSelectedToggleButton!!)
        }
    }
    @Synchronized
    private fun trueMoveCallback(view: TextView)
    {
        view.setColor(this, R.color.trueMove, com.androidplot.R.color.ap_white)

        val value = mSelectedToggleButton!!.text.toString().toInt()
        view.isClickable = false
        clearSelects()

        sudokuMatrix.decreaseNumberCount(value)
        if (sudokuMatrix.getNumberCounts()[value - 1] == 0)
            mBinding.linearLayoutButtons.children.elementAt(value - 1).visibility = View.INVISIBLE

        Handler(Looper.myLooper()!!).postDelayed({view.setColor(this)}, 2000)
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
        view.setColor(this, R.color.falseMove, com.androidplot.R.color.ap_white)
        clearSelects()
        Handler(Looper.myLooper()!!).postDelayed(
            {view.setColor(this)
                gamePlay.apply {
                    if (checkIfExistErrorCount()) {
                        errorDone()
                        view.setColor(this@MainActivity)
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
        if (sudokuMatrix.isTrueValue(value, index))
            runOnUiThread { trueMoveCallback(mSelectedTextView!!) }
        else
            runOnUiThread { falseMoveCallback(mSelectedTextView!!) }
    }
    private fun stopGame()
    {

    }
    private fun setLineBackground(index: Int)
    {
        var tableRow: TableRow
        var textView: TextView
        for (i in 0..<mBinding.tableLayoutMain.size) {
            tableRow = mBinding.tableLayoutMain[i] as TableRow
            for (k in 0..<tableRow.size) {
                textView = tableRow[k] as TextView
                if (index != i * 10 + k && index % 10 == k || index / 10 == i)
                    textView.setColor(this, backgroundColor = R.color.line_color)
                else
                    textView.setColor(this)
            }
        }
    }
    fun toggleButtonClicked(toggleButton: ToggleButton)
    {
        mSelectedToggleButton = toggleButton

        if (mSelectedTextView != null) {
            mSelectedTextView!!.text = toggleButton.text
            evaluateTheMove(mSelectedTextView!!, toggleButton)
        }
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
        val current = gamePlay.isNoteModeActive()
        gamePlay.setNoteMode(!current)
        mBinding.textViewNoteMode.text = getText(
            if (current)
                R.string.textview_note_mode_active_text
            else
                R.string.textview_note_mode_inactive_text)
    }
    fun buttonHintClicked()
    {
        runOnUiThread {
            val random = Random
            val index = random.nextInt(1, 9) * 10 + random.nextInt(1, 9)
            val value = sudokuMatrix.getValue(index)

            if (value.isEmpty()) {
                buttonHintClicked()
                return@runOnUiThread
            }

            ((mBinding.tableLayoutMain[index / 10] as TableRow)[index % 10] as TextView).text =
                value
        }
    }
    fun buttonUserClicked()
    {

    }
    fun buttonRestartClicked()
    {

    }
}