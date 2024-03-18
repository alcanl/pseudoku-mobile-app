package com.alcanl.sudoku.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alcanl.sudoku.repository.entity.gameplay.GameInfo
import java.time.LocalDate

@Entity(tableName = "users")
data class User(@PrimaryKey @ColumnInfo("user_name")var username: String = "",
                var password: String = "",
                var name: String = "",
                @ColumnInfo("last_name") var surName: String = "",
                @ColumnInfo("birth_date") var birthDate : LocalDate = LocalDate.now(),
                @ColumnInfo("register_date") var registerDate : LocalDate = LocalDate.now(),
                @ColumnInfo("e_mail") var eMail: String = "",
                var level: Int = 1,
                var games: MutableList<GameInfo>? = null
){
    override fun toString(): String {
        return String.format("$name $surName")
    }
}
