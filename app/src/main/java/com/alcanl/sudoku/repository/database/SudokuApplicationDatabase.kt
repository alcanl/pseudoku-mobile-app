package com.alcanl.sudoku.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alcanl.sudoku.repository.dao.IGamePlayDao
import com.alcanl.sudoku.repository.dao.IUserDao
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameplay.GamePlay

@Database(entities = [User::class, GamePlay::class], version = 1, exportSchema = false)
abstract class SudokuApplicationDatabase : RoomDatabase() {
    abstract fun createUserDao() : IUserDao
    abstract fun createGamePlayDao() : IGamePlayDao
}