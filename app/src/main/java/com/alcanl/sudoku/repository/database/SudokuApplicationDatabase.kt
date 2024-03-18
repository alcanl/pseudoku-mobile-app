package com.alcanl.sudoku.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alcanl.sudoku.repository.dao.IGameInfoDao
import com.alcanl.sudoku.repository.dao.IUserDao
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameplay.GameInfo

@Database(entities = [User::class, GameInfo::class], version = 1, exportSchema = false)
abstract class SudokuApplicationDatabase : RoomDatabase() {
    abstract fun createUserDao() : IUserDao
    abstract fun createGameInfoDao() : IGameInfoDao
}