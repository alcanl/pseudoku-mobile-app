package com.alcanl.sudoku.di.module.gameplay

import com.alcanl.sudoku.repository.entity.gameplay.GameInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object GameInfoModule {
    @Provides
    fun createGameInfo() : GameInfo = GameInfo()
}