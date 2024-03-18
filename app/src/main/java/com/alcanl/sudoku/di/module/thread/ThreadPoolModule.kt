package com.alcanl.sudoku.di.module.thread

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThreadPoolModule {
    @Provides
    @Singleton
    fun createThreadPool() : ExecutorService = Executors.newCachedThreadPool()
    @Provides
    @Singleton
    fun createScheduleThreadPool() : ScheduledExecutorService = Executors.newScheduledThreadPool(3)
}