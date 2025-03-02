package com.alcanl.sudoku

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityMainBinding
import com.alcanl.sudoku.dialog.SettingsDialog
import com.alcanl.sudoku.dialog.UserDialog
import com.alcanl.sudoku.dialog.showLoseAndPlayAgainDialog
import com.alcanl.sudoku.dialog.showWinAndPlayAgainDialog
import com.alcanl.sudoku.global.FRAME_LAYOUT_ID_START_INDEX
import com.alcanl.sudoku.global.ONE_SPACE_STRING
import com.alcanl.sudoku.global.extension.clearFrame
import com.alcanl.sudoku.global.extension.clearColor
import com.alcanl.sudoku.global.extension.clearTableBackground
import com.alcanl.sudoku.global.extension.getMoveInfo
import com.alcanl.sudoku.global.extension.getSelectedFrame
import com.alcanl.sudoku.global.extension.markAsFalseMove
import com.alcanl.sudoku.global.extension.markAsTrueMove
import com.alcanl.sudoku.global.extension.markTheNote
import com.alcanl.sudoku.global.extension.setLineColor
import com.alcanl.sudoku.global.extension.setSelectedColor
import com.alcanl.sudoku.global.extension.setTheme
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
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var threadPool: ExecutorService

    @Inject
    lateinit var chronometerCounter: ChronometerCounter

    @Inject
    lateinit var sudokuMatrix: SudokuMatrix

    @Inject
    lateinit var gameInfo: GameInfo

    @Inject
    lateinit var applicationService: SudokuApplicationDataService

    @Inject
    lateinit var handler: Handler

    private var mSelectedCell : FrameLayout? = null
    private var mSelectedToggleButton : ToggleButton? = null
    private lateinit var mUserDialog: UserDialog
    private lateinit var mSettingsDialog : SettingsDialog
    private lateinit var mUser : User
    private lateinit var mBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        getUserInfo()
        initialize()
    }
    override fun onResume()
    {
        super.onResume()

        chronometerCounter.apply {
            if (isCounterPaused())
                resumeCounter()
        }
    }
    override fun onPause()
    {
        super.onPause()

        chronometerCounter.apply {
            if (!isCounterPaused())
                pauseCounter()
        }
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
        chronometerCounter.startAndVisualizeCounter(mBinding)
    }

    private fun initBinding()
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.viewModel = MainActivityListenersViewModel(this)
        mBinding.matrix = sudokuMatrix
        mBinding.gamePlay = gameInfo
        mBinding.user = mUser
    }
    private fun tableCellClickedCallback(frameLayout: FrameLayout)
    {
        if (mSelectedCell != null && !gameInfo.checkLastMove())
            (mSelectedCell?.get(0) as TextView).text = ONE_SPACE_STRING

        mSelectedCell = frameLayout
        runOnUiThread { setLineBackground(resources.getResourceEntryName(mSelectedCell!!.id)
            .substring(FRAME_LAYOUT_ID_START_INDEX).toInt()) }
    }

    @Synchronized
    private fun trueMoveCallback(frameLayout: FrameLayout)
    {
        frameLayout.markAsTrueMove(this, handler)
        val (index, value) = frameLayout.getMoveInfo(mSelectedToggleButton!!)
        handleCorrectMoveOnMatrix(index, value)
        handleCorrectMoveOnGamePlay(index, value)
        mBinding.invalidateAll()
        mSelectedCell = null

        if (sudokuMatrix.isCompleted()) {
            handleWin()
            return
        }
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
    private fun falseMoveCallback(frameLayout: FrameLayout)
    {
        frameLayout.markAsFalseMove(this)
        val (index, value) = frameLayout.getMoveInfo(mSelectedToggleButton!!)

        if (gameInfo.checkIfExistErrorCount()) {
            sudokuMatrix.setCell(index, value.toInt())
            gameInfo.apply {
                errorDone()
                getIncorrectMoveScore()
                saveMove(Triple(index, value, false))
            }
            mSelectedToggleButton = null
            handler.postDelayed({sudokuMatrix.clearCell(index); mBinding.invalidateAll()}, 2000 )
        }
        else
            stopGame()
    }
    @Synchronized
    private fun buttonRestartCallback()
    {
        gameInfo.restart()
        chronometerCounter.restartCounter()
        sudokuMatrix.resetCurrentMatrix()
        runOnUiThread { mBinding.tableLayoutMain.clearTableBackground(this) }
        runOnUiThread(this::clearToggleButtons)
        mBinding.invalidateAll()
    }

    @Synchronized
    private fun buttonHintCallback()
    {
        if (!gameInfo.checkIfExistHintCount())
            return

        gameInfo.useHint()
        val (index, value) = sudokuMatrix.getHint()
        val frameLayout = (mBinding.tableLayoutMain[index / 10] as TableRow)[index % 10] as FrameLayout
        mSelectedToggleButton = mBinding.linearLayoutButtons[value.toInt() - 1] as ToggleButton

        runOnUiThread {
            tableCellClickedCallback(frameLayout)
            trueMoveCallback(frameLayout)
            mBinding.invalidateAll()
        }
    }
    @Synchronized
    private fun buttonUndoCallback()
    {
        try {
            val (index, value, isTrue) = gameInfo.useUndo()
            val frameLayout = mBinding.tableLayoutMain.getSelectedFrame(index)

            sudokuMatrix.apply {
                clearCell(index)
                increaseNumberCount(value.toInt())
            }
            runOnUiThread {
                mSelectedCell?.clearFrame()
                tableCellClicked(frameLayout)
                if (isTrue)
                    frameLayout.markAsTrueMove(this@MainActivity, null)
                else
                    frameLayout.markAsFalseMove(this@MainActivity)

                frameLayout.isClickable = true
                mBinding.invalidateAll()
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
        val index = resources.getResourceEntryName(mSelectedCell!!.id)
            .substring(FRAME_LAYOUT_ID_START_INDEX).toInt()
        if (sudokuMatrix.isTrueValue(value, index))
            runOnUiThread { trueMoveCallback(mSelectedCell!!) }
        else
            runOnUiThread { falseMoveCallback(mSelectedCell!!) }
    }
    private fun stopGame()
    {
        chronometerCounter.stopCounter()

        gameInfo.apply {
            isWin(false)
            setGameDuration(chronometerCounter.toString())
        }

        showLoseAndPlayAgainDialog(this, ::startNewGame) { finish() }
    }
    private fun setLineBackground(index: Int)
    {
        var tableRow: TableRow
        var frameLayout: FrameLayout
        for (i in 0..<mBinding.tableLayoutMain.size) {
            tableRow = mBinding.tableLayoutMain[i] as TableRow
            for (k in 0..<tableRow.size) {
                frameLayout = tableRow[k] as FrameLayout
                if (index == i * 10 + k)
                    frameLayout.setSelectedColor(this)
                else if (index % 10 == k || index / 10 == i)
                    frameLayout.setLineColor(this)
                else
                    frameLayout.clearColor(this)
            }
        }
    }
    private fun handleWin()
    {
        gameInfo.isWin(true)
        gameInfo.setGameDuration(chronometerCounter.toString())
        threadPool.execute { applicationService.saveGameInfo(gameInfo) }
        showWinAndPlayAgainDialog(this, ::startNewGame) { finish() }
    }
    private fun startNewGame()
    {
        threadPool.execute {
            gameInfo = GameInfo()
            sudokuMatrix.generateNewMatrix()
            initCounter()
            mSelectedCell = null
            mSelectedToggleButton = null
            runOnUiThread { mBinding.tableLayoutMain.clearTableBackground(this) }
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

        if (mSelectedCell != null)
            setLineBackground(resources.getResourceEntryName(mSelectedCell!!.id)
                .substring(FRAME_LAYOUT_ID_START_INDEX).toInt())
    }

    fun toggleButtonClicked(toggleButton: ToggleButton)
    {
        if (mSelectedCell == null)
            return

        if (gameInfo.isNoteModeActive()) {
            mSelectedCell?.markTheNote(toggleButton)
            return
        }

        mSelectedToggleButton = toggleButton
        evaluateTheMove()
    }

    fun tableCellClicked(frameLayout: FrameLayout)
    {
        runOnUiThread { tableCellClickedCallback(frameLayout) }
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