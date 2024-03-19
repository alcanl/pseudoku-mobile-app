package com.alcanl.sudoku.di.module.database

import android.content.Context
import androidx.room.Room
import com.alcanl.sudoku.repository.database.SudokuApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun createSudokuApplicationDatabase(@ApplicationContext context: Context) : SudokuApplicationDatabase
    {
        return Room.databaseBuilder(context, SudokuApplicationDatabase::class.java, "pseudokudb.sqlite3").build()
    }
}