package com.alcanl.sudoku.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo
import com.alcanl.sudoku.repository.entity.relation.UserToGameInfo

@Dao
interface IGameInfoDao {
    @Insert
    fun save(gameInfo: GameInfo)
    @Query("""SELECT * FROM game_info gi WHERE gi.id = :id""")
    fun findById(id: Long) : List<GameInfo>?
    @Transaction
    @Query("""SELECT * FROM users u WHERE u.user_name = :username""")
    fun findByUsername(username: String) : List<UserToGameInfo>?

}