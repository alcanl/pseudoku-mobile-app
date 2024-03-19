package com.alcanl.sudoku.repository.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo

class UserToGameInfo(@Embedded val user: User,
                     @Relation(parentColumn = "user_name", entityColumn = "user_name")
                     var gameInfoList: List<GameInfo>)
