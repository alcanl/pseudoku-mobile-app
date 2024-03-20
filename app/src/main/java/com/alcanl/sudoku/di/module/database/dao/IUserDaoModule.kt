package com.alcanl.sudoku.di.module.database.dao

import com.alcanl.sudoku.repository.dao.IUserDao
import com.alcanl.sudoku.repository.database.SudokuApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IUserDaoModule {
    @Provides
    @Singleton
    fun createUserDao(database: SudokuApplicationDatabase) : IUserDao
    {
        return database.createUserDao()
    }
}