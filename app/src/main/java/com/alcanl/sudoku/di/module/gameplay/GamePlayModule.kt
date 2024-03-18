package com.alcanl.sudoku.di.module.gameplay

import com.alcanl.sudoku.repository.entity.gameplay.GamePlay
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object GamePlayModule {
    @Provides
    fun createGamePlay() : GamePlay = GamePlay()
}