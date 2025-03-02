package com.alcanl.sudoku.timer

import com.alcanl.android.app.sudoku.databinding.ActivityMainBinding
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class ChronometerCounter @Inject constructor() {

    private var mSeconds = AtomicInteger(0)

    private var mMinutes = AtomicInteger(0)

    private var mHours = AtomicInteger(0)

    private lateinit var mChronometerCounterFuture: Future<*>

    @Volatile
    private var mIsPaused = false

    @Inject
    lateinit var scheduledExecutorService: ScheduledExecutorService

    private fun startCounterCallback(binding: ActivityMainBinding)
    {
        if (!mChronometerCounterFuture.isCancelled && !mIsPaused) {
            mSeconds.incrementAndGet()
            if (mSeconds.get() == 60) {
                mMinutes.incrementAndGet()
                mSeconds.set(0)
            }
            if (mMinutes.get() == 60) {
                mHours.incrementAndGet()
                mMinutes.set(0)
            }
            binding.timer = this.toString()
        }
    }

    fun startAndVisualizeCounter(binding: ActivityMainBinding)
    {
        restartCounter()
        mChronometerCounterFuture = scheduledExecutorService.scheduleWithFixedDelay(
            { startCounterCallback(binding) }, 0L, 1L, TimeUnit.SECONDS)
    }

    fun restartCounter()
    {
        mSeconds.set(0); mMinutes.set(0); mHours.set(0)
    }

    fun stopCounter()
    {
        mChronometerCounterFuture.cancel(true)
    }

    fun pauseCounter()
    {
        mIsPaused = true
    }

    fun resumeCounter()
    {
        mIsPaused = false
    }

    fun isCounterPaused() = mIsPaused

    override fun toString(): String
    {
        return "%02d:%02d:%02d".format(mHours.get(), mMinutes.get(), mSeconds.get())
    }

}