package com.alcanl.sudoku.service

import com.alcanl.sudoku.repository.RepositoryException
import com.alcanl.sudoku.repository.dal.SudokuApplicationHelper
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo
import com.alcanl.sudoku.repository.entity.relation.UserToGameInfo
import javax.inject.Inject

class SudokuApplicationDataService @Inject constructor(
    sudokuApplicationHelper: SudokuApplicationHelper
) {
    private val mSudokuApplicationHelper = sudokuApplicationHelper

    fun checkAndSaveUserByUsername(user: User) : Boolean
    {
        var result = false
        try {
            if (!mSudokuApplicationHelper.existUserByUsername(user.username)) {
                mSudokuApplicationHelper.saveUser(user)
                result = true
            }
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }
        return result
    }
    fun checkAndSaveUserByEmail(user: User) : Boolean
    {
        var result = false
        try {
            if (!mSudokuApplicationHelper.existUserByEmail(user.eMail)) {
                mSudokuApplicationHelper.saveUser(user)
                result = true
            }
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }
        return result
    }
    fun saveGameInfo(gameInfo: GameInfo)
    {
        try {
            mSudokuApplicationHelper.saveGameInfo(gameInfo)
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }

    }
    fun findUserByUsernameAndPassword(username: String, password: String) : User?
    {
        try {
            if (mSudokuApplicationHelper.existByUsernameAndPassword(username, password))
                return mSudokuApplicationHelper.findUserByUsernameAndPassword(username, password)

            return null
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }
    }
    fun findUserByEmailAndPassword(eMail: String, password: String): User?
    {
        try {
            if (mSudokuApplicationHelper.existByEmailAndPassword(eMail, password))
                return mSudokuApplicationHelper.findUserByEmailAndPassword(eMail, password)

            return null
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }
    }
    fun findGameInfoByUsername(username: String) : UserToGameInfo?
    {
        try {
            return mSudokuApplicationHelper.findGameInfoByUsername(username)
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }
    }
    fun deleteUser(user: User)
    {
        try {
            mSudokuApplicationHelper.deleteUser(user)
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }

    }
    fun updateUser(user: User)
    {
        try {
            mSudokuApplicationHelper.updateUser(user)
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }

    }
    fun findGameInfoById(id: Long)
    {
        try {
            mSudokuApplicationHelper.findGameInfoById(id)
        } catch (ex: RepositoryException) {
            throw ServiceException(ex)
        }
    }

}