package com.alcanl.sudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityMainBinding
import com.alcanl.sudoku.di.module.gameplay.GameInfoModule
import com.alcanl.sudoku.service.SudokuMatrix
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo
import com.alcanl.sudoku.global.disableNoteMode
import com.alcanl.sudoku.global.enableNoteMode
import com.alcanl.sudoku.global.getMoveInfo
import com.alcanl.sudoku.global.setColor
import com.alcanl.sudoku.repository.dal.SudokuApplicationHelper
import com.alcanl.sudoku.service.SudokuApplicationDataService
import com.alcanl.sudoku.timer.ChronometerCounter
import com.alcanl.sudoku.viewmodel.MainActivityListenersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.EmptyStackException
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
    lateinit var gameInfo: GameInfo
    @Inject
    lateinit var user: User
    @Inject
    lateinit var service: SudokuApplicationDataService
    private var mSelectedTextView : TextView? = null
    private var mSelectedToggleButton : ToggleButton? = null
    private lateinit var mBinding : ActivityMainBinding
    private lateinit var mCounter : Thread

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
        mCounter = Thread.currentThread()
        if (!mCounter.isInterrupted) {
            chronometerCounter.handleCounter()
            mBinding.timer = chronometerCounter.toString()
        }
    }
    private fun initBinding()
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = MainActivityListenersViewModel(this)
        mBinding.matrix = sudokuMatrix
        mBinding.gamePlay = gameInfo
        mBinding.user = user
    }
    private fun tableCellClickedCallback(textView: TextView)
    {
        mSelectedTextView = textView
        runOnUiThread { setLineBackground(resources.getResourceEntryName(textView.id)
            .substring(8).toInt()) }

        if (gameInfo.isNoteModeActive())
            tableCellClickedCallbackNoteModeOn(textView)

    }
    private fun tableCellClickedCallbackNoteModeOn(textView: TextView)
    {
        textView.enableNoteMode(this)
    }
    @Synchronized
    private fun trueMoveCallback(textView: TextView)
    {
        textView.setTextColor(getColor(R.color.trueMove))
        val (index, value) = textView.getMoveInfo(mSelectedToggleButton!!)
        textView.isClickable = false
        handleCorrectMoveOnMatrix(index, value)
        handleCorrectMoveOnGamePlay(index, value)
        mBinding.invalidateAll()
        mSelectedTextView = null

        if (sudokuMatrix.isCompleted()) {
            handleWin()
            return
        }

        Handler(Looper.myLooper()!!).postDelayed({textView.setTextColor(getColor(com.androidplot.R.color.ap_black))}, 2000)
    }
    private fun handleCorrectMoveOnMatrix(index: Int, value: String)
    {
        sudokuMatrix.apply {
            setCell(index, value.toInt())
            decreaseNumberCount(value.toInt())
            if (isAvailableValueCountOver(value.toInt()))
                mBinding.linearLayoutButtons.children.elementAt(value.toInt() - 1).visibility = View.INVISIBLE
        }
    }
    private fun handleCorrectMoveOnGamePlay(index: Int, value: String)
    {
        gameInfo.apply {
            saveMove(Triple(index, value, true))
            if (!isHintMove()) {
                getCorrectMoveScore()
            }
            else
                clearHintMove()
        }
    }
    @Synchronized
    private fun falseMoveCallback(textView: TextView)
    {
        textView.setTextColor(getColor(R.color.falseMove))
        val (index, value) = textView.getMoveInfo(mSelectedToggleButton!!)
        if (gameInfo.checkIfExistErrorCount()) {
            sudokuMatrix.setCell(index, value.toInt())
            gameInfo.apply {
                errorDone()
                getIncorrectMoveScore()
                saveMove(Triple(index, value, false))
            }
            mBinding.invalidateAll()
        }
        else
            stopGame()
    }
    @Synchronized
    private fun buttonRestartCallback()
    {
        gameInfo.restart()
        chronometerCounter.clearTimer()
        sudokuMatrix.resetCurrentMatrix()
        runOnUiThread(this::clearTableBackgroundCallback)
        mBinding.invalidateAll()
    }
    private fun clearTableBackgroundCallback()
    {
        mBinding.tableLayoutMain.children.forEach { view ->
            (view as TableRow).forEach {
                (it as TextView).setColor(this@MainActivity)
            }
        }
    }
    @Synchronized
    private fun buttonHintCallback()
    {
        if (!gameInfo.checkIfExistHintCount())
            return

        gameInfo.useHint()
        val (index, value) = sudokuMatrix.getHint()
        val textView = (mBinding.tableLayoutMain[index / 10] as TableRow)[index % 10] as TextView
        mSelectedToggleButton = mBinding.linearLayoutButtons[value.toInt() - 1] as ToggleButton

        runOnUiThread {
            tableCellClickedCallback(textView)
            trueMoveCallback(textView)
            mBinding.invalidateAll()
        }
    }
    @Synchronized
    private fun buttonUndoCallback()
    {
        try {
            val (index, value, isTrue) = gameInfo.useUndo()
            val textView =
                (mBinding.tableLayoutMain[index / 10] as TableRow)[index % 10] as TextView
            sudokuMatrix.apply {
                clearCell(index)
                increaseNumberCount(value.toInt())
            }
            runOnUiThread {
                mSelectedTextView?.text = ""
                mBinding.apply {
                    tableCellClicked(textView)
                    textView.setTextColor(getColor(if (isTrue) R.color.trueMove else R.color.falseMove))
                    textView.isClickable = true
                    invalidateAll()
                }
            }
        } catch (_: EmptyStackException) {
            runOnUiThread {
                Toast.makeText(this, "No Any Move Info", Toast.LENGTH_SHORT).show()
            }
        }
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
        gameInfo.apply {
            isWin(false)
            setGameDuration(chronometerCounter.toString())
        }
        mCounter.interrupt()

        AlertDialog.Builder(this@MainActivity)
            .setCancelable(false)
            .setTitle("Game Over")
            .setNeutralButton("OK") { _, _ -> }
            .setMessage("Sorry, You made 3 mistakes and nothing left.")
            .create().show()
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
    private fun handleWin()
    {
        gameInfo.isWin(true)
        gameInfo.setGameDuration(chronometerCounter.toString())
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setPositiveButton("Yes") {_,_ -> startNewGame()}
            .setNegativeButton("No") {_,_ -> finish()}
            .setTitle("Congratulations, Its a win!")
            .setMessage("Do you want to play a new game?")
            .create().show()
    }
    private fun startNewGame()
    {
        threadPool.execute {
            gameInfo = GameInfoModule.createGameInfo()
            sudokuMatrix.generateNewMatrix()
            mCounter.interrupt()
            chronometerCounter.clearTimer()
            initCounter()
            mSelectedTextView = null
            mSelectedToggleButton = null
            runOnUiThread(this::clearTableBackgroundCallback)
            mBinding.invalidateAll()
        }
    }
    fun toggleButtonClicked(toggleButton: ToggleButton)
    {
        if (mSelectedTextView == null || gameInfo.isNoteModeActive())
            return

        if (mSelectedTextView?.isSelected!!)
            mSelectedTextView?.disableNoteMode(this)

        mSelectedToggleButton = toggleButton
        evaluateTheMove(mSelectedTextView!!, toggleButton)
    }
    fun tableCellClicked(textView: TextView)
    {
        runOnUiThread { tableCellClickedCallback(textView) }
    }
    fun buttonBackClicked()
    {
        TODO("Not implemented yet")
    }
    fun buttonSettingsClicked()
    {
        TODO("Not implemented yet")
    }
    fun buttonUndoClicked()
    {
        threadPool.execute(this::buttonUndoCallback)
    }
    fun buttonNoteClicked()
    {
        val onOrOff = !gameInfo.isNoteModeActive()
        gameInfo.setNoteMode(onOrOff)
        mBinding.textViewNoteMode.text = getText(
            if (onOrOff)
                R.string.textview_note_mode_active_text
            else
                R.string.textview_note_mode_inactive_text)
    }
    fun buttonHintClicked()
    {
        threadPool.execute(this::buttonHintCallback)
    }
    fun buttonUserClicked()
    {
        TODO("Not implemented yet")
    }

    fun buttonRestartClicked()
    {
        threadPool.execute(this::buttonRestartCallback)
    }
}