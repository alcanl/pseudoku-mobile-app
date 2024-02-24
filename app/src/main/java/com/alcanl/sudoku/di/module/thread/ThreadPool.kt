package com.alcanl.sudoku.di.module.thread

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Module
@InstallIn(SingletonComponent::class)
class ThreadPool {
    @Provides
    fun createThreadPool() : ExecutorService = Executors.newCachedThreadPool()
}