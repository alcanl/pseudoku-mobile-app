package com.alcanl.sudoku.repository.dal

import com.alcanl.sudoku.repository.RepositoryException
import com.alcanl.sudoku.repository.dao.IGameInfoDao
import com.alcanl.sudoku.repository.dao.IUserDao
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo
import com.alcanl.sudoku.repository.entity.relation.UserToGameInfo
import javax.inject.Inject

class SudokuApplicationHelper @Inject constructor() {
    @Inject
    lateinit var userDao: IUserDao
    @Inject
    lateinit var gameInfoDao: IGameInfoDao

    fun existUserByUsername(username: String) : Boolean
    {
        try {
            return userDao.existById(username)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun existUserByEmail(eMail: String) : Boolean
    {
        try {
            return userDao.existByEmail(eMail)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun existByUsernameAndPassword(username: String, password: String) : Boolean
    {
        try {
            return userDao.existByUserNameAndPassword(username, password)
        }   catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun existByEmailAndPassword(eMail: String, password: String) : Boolean
    {
        try {
            return userDao.existByEmailAndPassword(eMail, password)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun findUserByUsernameAndPassword(username: String, password: String) : User?
    {
        try {
            return userDao.findByUserNameAndPassword(username, password)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun findUserByEmailAndPassword(eMail: String, password: String) : User?
    {
        try {
            return userDao.findByEmailAndPassword(eMail, password)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun saveUser(user: User)
    {
        try {
            userDao.save(user)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun findGameInfoByUsername(username: String) : UserToGameInfo?
    {
        try {
            return gameInfoDao.findByUsername(username)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun saveGameInfo(gameInfo: GameInfo)
    {
        try {
            gameInfoDao.save(gameInfo)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun deleteUser(user: User)
    {
        try {
            userDao.delete(user)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun updateUser(user: User)
    {
        try {
            userDao.updateUser(user)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
    fun findGameInfoById(id: Long) : GameInfo
    {
        try {
            return gameInfoDao.findById(id)
        } catch (ex: Throwable) {
            throw RepositoryException(ex)
        }
    }
}