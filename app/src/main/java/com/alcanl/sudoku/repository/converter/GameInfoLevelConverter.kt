package com.alcanl.sudoku.repository.converter

import androidx.room.TypeConverter
import com.alcanl.sudoku.repository.entity.gameinfo.level.Level


class GameInfoLevelConverter {
    @TypeConverter
    fun toGameInfoLevel(level: Int) : Level
    {
        return when (level) {
            1 -> Level.EASY
            2 -> Level.MEDIUM
            else -> Level.HARD
        }
    }
    @TypeConverter
    fun toIntLevel(level: Level) : Int
    {
        return when (level) {
            Level.EASY -> 1
            Level.MEDIUM -> 2
            else -> 3
        }
    }
}