package com.alcanl.sudoku.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "users")
data class User(@PrimaryKey(autoGenerate = true) var id : Int,
                var username: String,
                var password: String,
                var name: String,
                @ColumnInfo("last_name") var surName: String,
                @ColumnInfo("birth_date") var birthDate : LocalDate,
                @ColumnInfo("register_date") var registerDate : LocalDate,
                @ColumnInfo("e_mail") var eMail: String,
                var level: Int ){
    override fun toString(): String {
        return String.format("$name $surName")
    }
}
