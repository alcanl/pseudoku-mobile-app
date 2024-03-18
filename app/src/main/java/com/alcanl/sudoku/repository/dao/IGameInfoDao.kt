package com.alcanl.sudoku.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.alcanl.sudoku.repository.entity.gameplay.GameInfo

@Dao
interface IGameInfoDao {
    @Insert
    fun save(gameInfo: GameInfo)
    @Delete
    fun delete(gameInfo: GameInfo)
    @Query("""SELECT * FROM game_info gi WHERE gi.id = :id""")
    fun findById(id: Long) : GameInfo?
}