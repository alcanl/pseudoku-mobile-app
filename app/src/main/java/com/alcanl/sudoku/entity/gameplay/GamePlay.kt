package com.alcanl.sudoku.entity.gameplay

import com.alcanl.sudoku.entity.level.Level
import java.util.Stack



data class GamePlay(private var mHintCount : Int = 3, private var mErrorCount: Int = 3, private var mResult: Boolean = false,
                    private var mMoveStack: Stack<Pair<Int, Int>> = Stack(), private var mEmptyBoxCounts: MutableMap<Int, Int> = HashMap(),
                    private var mGameDuration: Long = 0, private val mLevel: Level = Level.MEDIUM,
                    private var mIsNoteModeActive: Boolean = false, private var mScore: Int = 0) {

    init {
        (1..9).forEach { mEmptyBoxCounts[it] = 0 }
    }
    fun saveMove(pair: Pair<Int, Int>)
    {
        mMoveStack.push(pair)
    }
    fun useUndo()
    {
        mMoveStack.pop()
    }
    fun useHint()
    {
        if (mHintCount <= 0)
            return

        --mHintCount
    }
    fun checkIfExistHintCount() : Boolean = mHintCount > 0
    fun checkIfExistErrorCount() : Boolean = mErrorCount > 0
    fun errorDone()
    {
        if (mErrorCount <= 0)
            return

        --mErrorCount
    }
    fun setWin()
    {
        mResult = true
    }
    fun setGameDuration(duration: Long)
    {
        mGameDuration = duration
    }
    fun getHintCount() = mHintCount.toString()
    fun getLevel() = mLevel.toString()
    fun getErrorCount() = "$mErrorCount/3"
    fun isNoteModeActive() = mIsNoteModeActive
    fun setNoteMode(active: Boolean)
    {
        mIsNoteModeActive = active
    }
    fun getCurrentScore() = mScore.toString()
}