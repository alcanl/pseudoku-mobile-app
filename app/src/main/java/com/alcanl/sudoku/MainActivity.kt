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
import com.alcanl.sudoku.global.disableNoteMode
import com.alcanl.sudoku.global.enableNoteMode
import com.alcanl.sudoku.global.setColor
import com.alcanl.sudoku.timer.ChronometerCounter
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
    lateinit var chronometerCounter: ChronometerCounter
    @Inject
    lateinit var sudokuMatrix: SudokuMatrix
    @Inject
    lateinit var gamePlay: GamePlay
    @Inject
    lateinit var user: User
    private lateinit var mBinding : ActivityMainBinding
    private var mSelectedTextView : TextView? = null
    private var mSelectedToggleButton : ToggleButton? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        initialize()
        tableCellClicked(mBinding.textView0)
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
    private fun tableCellClickedCallback(textView: TextView)
    {
        if (gamePlay.isNoteModeActive()) {
            tableCellClickedCallbackNoteModeOn(textView)
            return
        }
        mSelectedTextView = textView
        runOnUiThread { setLineBackground(resources.getResourceEntryName(textView.id)
            .substring(8).toInt()) }
    }
    private fun tableCellClickedCallbackNoteModeOn(textView: TextView)
    {
        textView.enableNoteMode(this)
    }
    @Synchronized
    private fun trueMoveCallback(textView: TextView)
    {
        textView.setTextColor(getColor(R.color.trueMove))

        val value = mSelectedToggleButton!!.text.toString().toInt()
        textView.isClickable = false

        sudokuMatrix.decreaseNumberCount(value)
        sudokuMatrix.setCell(resources.getResourceEntryName(textView.id).substring(8).toInt(), value)

        if (sudokuMatrix.getNumberCounts()[value - 1] == 0)
            mBinding.linearLayoutButtons.children.elementAt(value - 1).visibility = View.INVISIBLE

        Handler(Looper.myLooper()!!).postDelayed({textView.setTextColor(getColor(com.androidplot.R.color.ap_black))}, 2000)
    }
    private fun clearSelects()
    {
        mSelectedTextView = null
        mSelectedToggleButton = null
    }
    @Synchronized
    private fun falseMoveCallback(view: TextView)
    {
        view.setTextColor(getColor(R.color.falseMove))
        clearSelects()
        Handler(Looper.myLooper()!!).postDelayed(
            { gamePlay.apply {
                    if (checkIfExistErrorCount()) {
                        errorDone()
                        view.text = ""
                        view.setTextColor(getColor(com.androidplot.R.color.ap_black))
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
                if (index == i * 10 + k)
                    textView.setColor(this, backgroundColor = R.color.aqua)
                else if (index % 10 == k || index / 10 == i)
                    textView.setColor(this, backgroundColor = R.color.line_color)
                else
                    textView.setColor(this)
            }
        }
    }
    fun toggleButtonClicked(toggleButton: ToggleButton)
    {
        if (mSelectedTextView == null || gamePlay.isNoteModeActive())
            return

        if (mSelectedTextView?.isSelected!!)
            mSelectedTextView?.disableNoteMode(this)

        mSelectedTextView!!.text = toggleButton.text
        evaluateTheMove(mSelectedTextView!!, toggleButton)
    }
    fun tableCellClicked(view: TextView)
    {
        runOnUiThread { tableCellClickedCallback(view) }
    }
    fun buttonBackClicked()
    {

    }
    fun buttonSettingsClicked()
    {

    }
    fun buttonUndoClicked()
    {
        val (index, value, isTrue) = gamePlay.useUndo()
        val textView = (mBinding.tableLayoutMain[index / 10] as TableRow)[index % 10] as TextView

        mBinding.apply {
            tableCellClicked(textView)
            textView.setTextColor(getColor(if (isTrue) R.color.trueMove else R.color.falseMove))
            textView.text = value
        }
    }
    fun buttonNoteClicked()
    {
        val current = gamePlay.isNoteModeActive()
        mBinding.textViewNoteMode.text = getText(
            if (current)
                R.string.textview_note_mode_active_text
            else
                R.string.textview_note_mode_inactive_text)
        gamePlay.setNoteMode(!current)


    }
    fun buttonHintClicked()
    {
        if (!gamePlay.checkIfExistHintCount())
            return

        runOnUiThread {
            val (index, value) = sudokuMatrix.getHint()

            ((mBinding.tableLayoutMain[index / 10] as TableRow)[index % 10] as TextView)
                .text = value

            gamePlay.saveMove(Triple(index, value, true))
            gamePlay.useHint()
        }
    }
    fun buttonUserClicked()
    {

    }
    fun buttonRestartClicked()
    {

    }
}