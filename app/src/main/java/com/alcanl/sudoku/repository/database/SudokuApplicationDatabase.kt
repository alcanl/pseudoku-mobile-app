package com.alcanl.sudoku.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alcanl.sudoku.repository.converter.GameInfoLevelConverter
import com.alcanl.sudoku.repository.converter.LocalDateConverter
import com.alcanl.sudoku.repository.dao.IGameInfoDao
import com.alcanl.sudoku.repository.dao.IUserDao
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo

@Database(entities = [User::class, GameInfo::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter::class, GameInfoLevelConverter::class)
abstract class SudokuApplicationDatabase : RoomDatabase() {
    abstract fun createUserDao() : IUserDao
    abstract fun createGameInfoDao() : IGameInfoDao
}