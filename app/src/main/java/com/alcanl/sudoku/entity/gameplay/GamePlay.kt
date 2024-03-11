package com.alcanl.sudoku.entity.gameplay

import java.util.Stack



data class GamePlay(private var mHintCount : Int = 3, private var mErrorCount: Int = 3, private var mResult: Boolean = false,
                    private var mMoveStack: Stack<Pair<Int, Int>> = Stack(), private var mEmptyBoxCounts: MutableMap<Int, Int> = HashMap(),
                    private var mGameDuration: Long = 0) {

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
    fun checkNumberCount(number: Int) : Int = mEmptyBoxCounts[number]!!
    fun useHint()
    {
        if (mHintCount <= 0)
            return

        --mHintCount
    }
    fun checkIfExistHintCount() : Boolean = mHintCount > 0
    fun checkIfExistErrorCount() : Boolean = mErrorCount > 0
    fun setWin()
    {
        mResult = true
    }
    fun setGameDuration(duration: Long)
    {
        mGameDuration = duration
    }
}