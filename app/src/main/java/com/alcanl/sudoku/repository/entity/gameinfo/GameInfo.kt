package com.alcanl.sudoku.repository.entity.gameinfo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.alcanl.sudoku.global.FALSE_SCORE_PER_MOVE
import com.alcanl.sudoku.global.TRUE_SCORE_PER_MOVE
import com.alcanl.sudoku.global.theme.BoardTheme
import com.alcanl.sudoku.global.theme.BoardTheme.*
import com.alcanl.sudoku.repository.entity.gameinfo.level.Level
import java.io.Serializable
import java.util.EmptyStackException
import java.util.Stack
@Entity("game_info")
data class GameInfo(@PrimaryKey(autoGenerate = true) var id: Long = 0L,
                    @ColumnInfo("hint_count")var hintCount : Int = 3,
                    @ColumnInfo("mistake_count")var errorCount: Int = 0,
                    @ColumnInfo("duration")var gameDuration: Long = 0L,
                    @ColumnInfo("user_name")var username: String = "",
                    var score: Int = 0,
                    var result: Boolean = false,
                    var level: Level = Level.MEDIUM,
                    @Ignore private var mActiveTheme: BoardTheme = THEME_DEFAULT,
                    @Volatile @Ignore private var mIsNoteModeActive: Boolean = false,
                    @Ignore private var mMoveStack: Stack<Triple<Int, String, Boolean>> = Stack()
                    ) : Serializable {
    @Ignore private var mHintMove = false

    fun saveMove(triple: Triple<Int, String, Boolean>)
    {
        mMoveStack.push(triple)
    }
    fun useUndo() : Triple<Int, String, Boolean>
    {
        return mMoveStack.pop()
    }
    fun useHint()
    {
        if (hintCount <= 0)
            return

        --hintCount
        mHintMove = true
    }
    fun checkIfExistHintCount() : Boolean = hintCount > 0
    fun checkIfExistErrorCount() : Boolean = errorCount < 3
    fun errorDone()
    {
        if (errorCount >= 3)
            return

        ++errorCount
    }
    fun isWin(result: Boolean)
    {
        this.result = result
    }
    fun setGameDuration(duration: String)
    {
        gameDuration = duration.substring(0,2).toLong() * 3600 + duration.substring(3, 5).toLong() * 60 + duration.substring(6).toLong()
    }

    fun isNoteModeActive() = mIsNoteModeActive
    fun setNoteMode(active: Boolean)
    {
        mIsNoteModeActive = active
    }
    fun getCurrentScore() = score.toString()
    fun getCurrentErrorCount() = "3/$errorCount"
    fun getCurrentHintCount() = "$hintCount"
    fun restart()
    {
        hintCount = 3
        result = false
        gameDuration = 0
        errorCount = 0
        mIsNoteModeActive = false
        mMoveStack.clear()
        score = 0
    }
    fun clearHintMove() { mHintMove = false }
    fun isHintMove() = mHintMove
    fun getCorrectMoveScore()
    {
        score += TRUE_SCORE_PER_MOVE
    }
    fun getIncorrectMoveScore()
    {
        score -= FALSE_SCORE_PER_MOVE
    }
    fun checkLastMove() : Boolean
    {
        return try {
            mMoveStack.peek().third
        } catch (_: EmptyStackException) {
            true
        }
    }
    fun setBoardTheme(boardTheme: BoardTheme)
    {
        mActiveTheme = boardTheme
    }
    fun activeTheme() = mActiveTheme
}