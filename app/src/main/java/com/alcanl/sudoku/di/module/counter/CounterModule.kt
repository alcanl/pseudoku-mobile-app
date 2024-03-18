package com.alcanl.sudoku.di.module.counter

import com.alcanl.sudoku.timer.ChronometerCounter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CounterModule {
    @Provides
    @Singleton
    fun createCounter() : ChronometerCounter = ChronometerCounter()
}