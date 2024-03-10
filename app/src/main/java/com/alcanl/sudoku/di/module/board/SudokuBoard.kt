package com.alcanl.sudoku.di.module.board

import com.alcanl.sudoku.entity.SudokuMatrix
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object SudokuBoard {
    @Provides
    fun createBoard() : SudokuMatrix = SudokuMatrix()
}