package com.alcanl.sudoku.entity.gameplay

import com.alcanl.sudoku.entity.level.Level
import java.util.Stack



data class GamePlay(private var mHintCount : Int = 3, private var mErrorCount: Int = 0, private var mResult: Boolean = false,
                    private var mMoveStack: Stack<Triple<Int, String, Boolean>> = Stack(),
                    private var mGameDuration: Long = 0, private val mLevel: Level = Level.MEDIUM,
                    private var mIsNoteModeActive: Boolean = false, private var mScore: Int = 0) {
    private var mHintMove = false

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
        if (mHintCount <= 0)
            return

        --mHintCount
        mHintMove = true
    }
    fun checkIfExistHintCount() : Boolean = mHintCount > 0
    fun checkIfExistErrorCount() : Boolean = mErrorCount <= 3
    fun errorDone()
    {
        if (mErrorCount >= 3)
            return

        ++mErrorCount
    }
    fun isWin(result: Boolean)
    {
        mResult = result
    }
    fun setGameDuration(duration: String)
    {
        mGameDuration = duration.substring(0,2).toLong() * 3600 + duration.substring(3, 5).toLong() * 60 + duration.substring(6).toLong()
    }
    fun getHintCount() = "$mHintCount"
    fun getLevel() = mLevel.toString()
    fun getErrorCount() = "$mErrorCount/3"
    fun isNoteModeActive() = mIsNoteModeActive
    fun setNoteMode(active: Boolean)
    {
        mIsNoteModeActive = active
    }
    fun getCurrentScore() = mScore.toString()
    fun createNewGamePlay()
    {
        mHintCount = 3
        mResult = false
        mGameDuration = 0
        mErrorCount = 0
        mIsNoteModeActive = false
        mMoveStack.clear()
        mScore = 0
    }
    fun clearHintMove() { mHintMove = false }
    fun isHintMove() = mHintMove
    fun getCorrectMoveScore()
    {
        mScore += 50
    }
    fun getIncorrectMoveScore()
    {
        mScore -= 100
    }
}