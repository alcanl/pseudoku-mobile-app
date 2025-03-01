package com.alcanl.sudoku

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityMainBinding
import com.alcanl.sudoku.dialog.SettingsDialog
import com.alcanl.sudoku.dialog.UserDialog
import com.alcanl.sudoku.global.clearColor
import com.alcanl.sudoku.global.disableNoteMode
import com.alcanl.sudoku.global.enableNoteMode
import com.alcanl.sudoku.global.getMoveInfo
import com.alcanl.sudoku.global.setLineColor
import com.alcanl.sudoku.global.setSelectedColor
import com.alcanl.sudoku.global.setTheme
import com.alcanl.sudoku.global.theme.BoardTheme
import com.alcanl.sudoku.global.theme.BoardTheme.*
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo
import com.alcanl.sudoku.service.SudokuApplicationDataService
import com.alcanl.sudoku.service.SudokuMatrix
import com.alcanl.sudoku.timer.ChronometerCounter
import com.alcanl.sudoku.viewmodel.MainActivityListenersViewModel
import com.google.android.material.button.MaterialButton
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
    lateinit var applicationService: SudokuApplicationDataService

    private var mSelectedTextView : TextView? = null
    private var mSelectedToggleButton : ToggleButton? = null
    private lateinit var mUserDialog: UserDialog
    private lateinit var mSettingsDialog : SettingsDialog
    private lateinit var mUser : User
    private lateinit var mBinding : ActivityMainBinding
    private lateinit var mChronometerCounterThread : Thread

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        getUserInfo()
        initialize()
    }
    override fun onResume()
    {
        super.onResume()
        chronometerCounter.resume()
    }
    override fun onPause()
    {
        super.onPause()
        chronometerCounter.pause()
    }
    @Suppress("DEPRECATION")
    private fun getUserInfo()
    {
        mUser = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra("user") as User
        else
            intent.getSerializableExtra("user", User::class.java)!!
    }
    private fun initialize()
    {
        initBinding()
        initCounter()
        mSettingsDialog = SettingsDialog(this)
    }
    private fun initCounter()
    {
        scheduledThreadPool.scheduleWithFixedDelay({ chronometerCounterCallback() }, 0, 1, TimeUnit.SECONDS)
    }
    private fun chronometerCounterCallback()
    {
        mChronometerCounterThread = Thread.currentThread()
        if (!mChronometerCounterThread.isInterrupted && !chronometerCounter.isPaused()) {
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
        mBinding.user = mUser
    }
    private fun tableCellClickedCallback(textView: TextView)
    {
        if (mSelectedTextView != null && !gameInfo.checkLastMove())
            mSelectedTextView?.text = " "

        mSelectedTextView = textView
        runOnUiThread { setLineBackground(resources.getResourceEntryName(mSelectedTextView!!.id)
            .substring(8).toInt()) }

        if (gameInfo.isNoteModeActive())
            tableCellClickedCallbackNoteModeOn(mSelectedTextView!!)

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
            textView.isClickable = true
            mSelectedToggleButton = null
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
        runOnUiThread(this::clearToggleButtons)
        mBinding.invalidateAll()
    }
    private fun clearTableBackgroundCallback()
    {
        mBinding.tableLayoutMain.children.forEach { view ->
            (view as TableRow).forEach {
                (it as TextView).clearColor(this@MainActivity)
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
                mSelectedTextView?.text = " "
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
    private fun evaluateTheMove()
    {
        val value = mSelectedToggleButton!!.text.toString().toInt()
        val index = resources.getResourceEntryName(mSelectedTextView!!.id).substring(8).toInt()
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
        mChronometerCounterThread.interrupt()

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
                    textView.setSelectedColor(this)
                else if (index % 10 == k || index / 10 == i)
                    textView.setLineColor(this)
                else
                    textView.clearColor(this)
            }
        }
    }
    private fun handleWin()
    {
        gameInfo.isWin(true)
        gameInfo.setGameDuration(chronometerCounter.toString())
        applicationService.saveGameInfo(gameInfo)
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
            gameInfo = GameInfo()
            sudokuMatrix.generateNewMatrix()
            mChronometerCounterThread.interrupt()
            chronometerCounter.clearTimer()
            initCounter()
            mSelectedTextView = null
            mSelectedToggleButton = null
            runOnUiThread(this::clearTableBackgroundCallback)
            runOnUiThread(this::clearToggleButtons)
            mBinding.invalidateAll()
        }
    }
    private fun clearToggleButtons()
    {
        mBinding.linearLayoutButtons.children.forEach { (it as ToggleButton).visibility = View.VISIBLE }
    }
    @Synchronized
    private fun setThemeCallback(theme: BoardTheme)
    {
        mBinding.tableLayoutMain.setTheme(this, theme)

        if (mSelectedTextView != null)
            setLineBackground(resources.getResourceEntryName(mSelectedTextView!!.id)
                .substring(8).toInt())
    }

    fun toggleButtonClicked(toggleButton: ToggleButton)
    {
        if (mSelectedTextView == null || gameInfo.isNoteModeActive())
            return

        if (mSelectedTextView?.isSelected!!)
            mSelectedTextView?.disableNoteMode(this)

        mSelectedToggleButton = toggleButton
        evaluateTheMove()
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
        mSettingsDialog = SettingsDialog(this).apply { show() }
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
        mUserDialog = UserDialog(this).apply { show() }
    }
    fun buttonRestartClicked()
    {
        threadPool.execute(this::buttonRestartCallback)
    }
    fun buttonSetThemeClicked(button: MaterialButton)
    {
        mSettingsDialog.dismiss()
        when (resources.getResourceEntryName(button.id)) {
            THEME_DEFAULT.toString() -> gameInfo.setBoardTheme(THEME_DEFAULT)
            THEME_DARK.toString() -> gameInfo.setBoardTheme(THEME_DARK)
            THEME_LIGHT.toString() -> gameInfo.setBoardTheme(THEME_LIGHT)
        }
        runOnUiThread { setThemeCallback(gameInfo.activeTheme()) }
    }
}