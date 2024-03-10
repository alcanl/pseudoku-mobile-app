package com.alcanl.sudoku.di.module.counter

import com.alcanl.sudoku.timer.TimeCounter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Counter {
    @Provides
    @Singleton
    fun createCounter() : TimeCounter = TimeCounter()
}