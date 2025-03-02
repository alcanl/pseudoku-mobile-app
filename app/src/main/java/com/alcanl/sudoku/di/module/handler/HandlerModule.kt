package com.alcanl.sudoku.di.module.handler

import android.os.Handler
import android.os.Looper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HandlerModule {

    @Provides
    fun createHandler() = Handler(Looper.getMainLooper())
}