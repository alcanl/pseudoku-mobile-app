package com.alcanl.sudoku.repository.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alcanl.sudoku.repository.entity.User

@Dao
interface IUserDao {
    @Query("""SELECT * FROM users u WHERE u.user_name = :username AND u.password = :password""")
    fun findByUserNameAndPassword(username: String, password: String) : User?
    @Query("""SELECT EXISTS(SELECT * FROM users u WHERE u.user_name = :username AND u.password = :password)""")
    fun existByUserNameAndPassword(username: String, password: String) : Boolean
    @Insert
    fun save(user: User)
    @Query("""SELECT EXISTS(SELECT * FROM users u WHERE u.user_name = :username)""")
    fun existById(username: String) : Boolean
    @Delete
    fun delete(user: User)
    @Update
    fun updateUsername(user: User)
}