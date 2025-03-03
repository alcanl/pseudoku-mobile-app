package com.alcanl.sudoku.di.module.handler

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.sync.Mutex

@Module
@InstallIn(SingletonComponent::class)
object MutexModule {

    @Provides
    fun createMutex() : Mutex = Mutex()
}