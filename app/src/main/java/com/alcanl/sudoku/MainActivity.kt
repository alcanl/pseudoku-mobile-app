package com.alcanl.sudoku

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.alcanl.android.app.sudoku.R
import com.alcanl.android.app.sudoku.databinding.ActivityMainBinding
import com.alcanl.sudoku.dialog.SettingsDialog
import com.alcanl.sudoku.dialog.UserDialog
import com.alcanl.sudoku.dialog.showLoseAndPlayAgainDialog
import com.alcanl.sudoku.dialog.showWinAndPlayAgainDialog
import com.alcanl.sudoku.global.FRAME_LAYOUT_ID_START_INDEX
import com.alcanl.sudoku.global.ONE_SPACE_STRING
import com.alcanl.sudoku.global.extension.clearFrame
import com.alcanl.sudoku.global.extension.clearTableBackground
import com.alcanl.sudoku.global.extension.getMoveInfo
import com.alcanl.sudoku.global.extension.getSelectedFrame
import com.alcanl.sudoku.global.extension.getSelectedToggleButton
import com.alcanl.sudoku.global.extension.hideCompletedNumber
import com.alcanl.sudoku.global.extension.markAsFalseMove
import com.alcanl.sudoku.global.extension.markAsTrueMove
import com.alcanl.sudoku.global.extension.markTheNote
import com.alcanl.sudoku.global.extension.refreshLayout
import com.alcanl.sudoku.global.extension.setLineBackGround
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.EmptyStackException
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

    private fun initCounter() = chronometerCounter.startAndVisualizeCounter(mBinding)

    private fun initBinding()
    {
        mBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .also {
                it.viewModel = MainActivityListenersViewModel(this@MainActivity)
                it.matrix = sudokuMatrix
                it.gamePlay = gameInfo
                it.user = mUser
            }
    }

    private fun tableCellClickedCallback(frameLayout: FrameLayout)
    {
        if (mSelectedCell != null && !gameInfo.checkLastMove())
            (mSelectedCell?.get(0) as TextView).text = ONE_SPACE_STRING

        mSelectedCell = frameLayout

        mBinding.tableLayoutMain
            .setLineBackGround(resources.getResourceEntryName(mSelectedCell!!.id)
                .substring(FRAME_LAYOUT_ID_START_INDEX).toInt(), this@MainActivity)
    }

    @Synchronized
    private fun trueMoveCallback(frameLayout: FrameLayout)
    {
        frameLayout.markAsTrueMove(this, handler)
        val (index, value) = frameLayout.getMoveInfo(mSelectedToggleButton!!)
        handleCorrectMoveOnMatrix(index, value.toInt())
        handleCorrectMoveOnGamePlay(index, value)
        mBinding.invalidateAll()
        mSelectedCell = null

        if (sudokuMatrix.isCompleted()) {
            handleWin()
            return
        }
    }

    private fun handleCorrectMoveOnMatrix(index: Int, value: Int)
    {
        sudokuMatrix.apply {
            setCell(index, value)
            decreaseNumberCount(value)
            if (isAvailableValueCountOver(value))
                mBinding.linearLayoutButtons.hideCompletedNumber(value)
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
            mBinding.invalidateAll()
            mSelectedToggleButton = null
            handler.postDelayed({ sudokuMatrix.clearCell(index); mBinding.invalidateAll() }, 2000 )
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
        lifecycleScope.launch(Dispatchers.Main) { mBinding.tableLayoutMain.clearTableBackground(this@MainActivity) }
        lifecycleScope.launch(Dispatchers.Main) { mBinding.linearLayoutButtons.refreshLayout() }
        mBinding.invalidateAll()
    }

    @Synchronized
    private fun buttonHintCallback()
    {
        if (!gameInfo.checkIfExistHintCount())
            return

        gameInfo.useHint()
        val (index, value) = sudokuMatrix.getHint()
        val frameLayout = mBinding.tableLayoutMain.getSelectedFrame(index)
        mSelectedToggleButton = mBinding.linearLayoutButtons.getSelectedToggleButton(value.toInt())

        lifecycleScope.launch(Dispatchers.Main) {
            tableCellClickedCallback(frameLayout)
            trueMoveCallback(frameLayout)
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
            lifecycleScope.launch(Dispatchers.Main) {
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
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "No Any Move Info", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun evaluateTheMove()
    {
        val value = mSelectedToggleButton!!.text.toString().toInt()
        val index = resources.getResourceEntryName(mSelectedCell!!.id)
            .substring(FRAME_LAYOUT_ID_START_INDEX).toInt()

        lifecycleScope.launch(Dispatchers.Main) {
            if (sudokuMatrix.isTrueValue(value, index))
                trueMoveCallback(mSelectedCell!!)
            else
                falseMoveCallback(mSelectedCell!!)
        }
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

    private fun handleWin()
    {
        gameInfo.isWin(true)
        gameInfo.setGameDuration(chronometerCounter.toString())
        lifecycleScope.launch(Dispatchers.IO) { applicationService.saveGameInfo(gameInfo) }
        showWinAndPlayAgainDialog(this, ::startNewGame) { finish() }
    }

    private fun startNewGame()
    {
        lifecycleScope.launch(Dispatchers.Default) {
            gameInfo = GameInfo()
            sudokuMatrix.generateNewMatrix()
            initCounter()
            mSelectedCell = null
            mSelectedToggleButton = null
            launch(Dispatchers.Main) { mBinding.tableLayoutMain.clearTableBackground(this@MainActivity) }
            launch(Dispatchers.Main) { mBinding.linearLayoutButtons.refreshLayout() }
            mBinding.invalidateAll()
        }
    }

    @Synchronized
    private fun setThemeCallback(theme: BoardTheme)
    {
        mBinding.tableLayoutMain.setTheme(this, theme)

        if (mSelectedCell != null)
            mBinding.tableLayoutMain
                .setLineBackGround(resources.getResourceEntryName(mSelectedCell!!.id)
                    .substring(FRAME_LAYOUT_ID_START_INDEX).toInt(), this)

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
        lifecycleScope.launch(Dispatchers.Main) { tableCellClickedCallback(frameLayout) }
    }

    fun buttonBackClicked()
    {
        TODO("Not implemented yet")
    }

    fun buttonSettingsClicked()
    {
        mSettingsDialog = SettingsDialog(this).apply { show() }
    }

    fun buttonUndoClicked() = lifecycleScope.launch(Dispatchers.Default) { buttonUndoCallback() }

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

    fun buttonHintClicked() = lifecycleScope.launch(Dispatchers.Default) { buttonHintCallback() }

    fun buttonUserClicked()
    {
        mUserDialog = UserDialog(this).apply { show() }
    }

    fun buttonRestartClicked() = lifecycleScope.launch(Dispatchers.Default) { buttonRestartCallback() }


    fun buttonSetThemeClicked(button: MaterialButton)
    {
        mSettingsDialog.dismiss()
        when (resources.getResourceEntryName(button.id)) {
            THEME_DEFAULT.toString() -> gameInfo.setBoardTheme(THEME_DEFAULT)
            THEME_DARK.toString() -> gameInfo.setBoardTheme(THEME_DARK)
            THEME_LIGHT.toString() -> gameInfo.setBoardTheme(THEME_LIGHT)
        }
        lifecycleScope.launch(Dispatchers.Main) { setThemeCallback(gameInfo.activeTheme()) }
    }
}