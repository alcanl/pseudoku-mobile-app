package com.alcanl.sudoku.di.module.database.dao

import com.alcanl.sudoku.repository.dao.IGameInfoDao
import com.alcanl.sudoku.repository.database.SudokuApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IGameInfoDaoModule {
    @Singleton
    @Provides
    fun createGameInfoDao(database: SudokuApplicationDatabase) : IGameInfoDao
    {
        return database.createGameInfoDao()
    }
}