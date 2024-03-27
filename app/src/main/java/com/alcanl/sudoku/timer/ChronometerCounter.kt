package com.alcanl.sudoku.timer

class ChronometerCounter {
    private var mSeconds = 0
    private var mMinutes = 0
    private var mHours = 0
    private var mIsPaused = false
    fun handleCounter()
    {
        ++mSeconds
        if (mSeconds == 60) {
            ++mMinutes
            mSeconds = 0
        }
        if (mMinutes == 60) {
            ++mHours
            mMinutes = 0
        }
    }
    fun clearTimer()
    {
        mSeconds = 0; mMinutes = 0; mHours = 0
    }
    fun pause()
    {
        mIsPaused = true
    }
    fun resume()
    {
        mIsPaused = false
    }
    fun isPaused() : Boolean = mIsPaused
    override fun toString(): String
    {
        return String.format("%02d:%02d:%02d", mHours, mMinutes, mSeconds)
    }

}