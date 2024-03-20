package com.alcanl.sudoku.service

import com.alcanl.sudoku.repository.dal.SudokuApplicationHelper
import com.alcanl.sudoku.repository.entity.User
import com.alcanl.sudoku.repository.entity.gameinfo.GameInfo
import com.alcanl.sudoku.repository.entity.relation.UserToGameInfo
import javax.inject.Inject

class SudokuApplicationDataService @Inject constructor(
    sudokuApplicationHelper: SudokuApplicationHelper
) {
    private val mSudokuApplicationHelper = sudokuApplicationHelper

    fun checkAndSaveUser(user: User)
    {
        if (!mSudokuApplicationHelper.existUserByUsername(user.username))
            mSudokuApplicationHelper.saveUser(user)
    }
    fun saveGameInfo(gameInfo: GameInfo)
    {
        mSudokuApplicationHelper.saveGameInfo(gameInfo)
    }
    fun findUserByUsernameAndPassword(username: String, password: String) : User?
    {
        if (mSudokuApplicationHelper.existByUsernameAndPassword(username, password))
            return mSudokuApplicationHelper.findUserByUsernameAndPassword(username, password)

        return null
    }
    fun findGameInfoByUsername(username: String) : UserToGameInfo?
    {
        return mSudokuApplicationHelper.findGameInfoByUsername(username)
    }
    fun deleteUser(user: User)
    {
        mSudokuApplicationHelper.deleteUser(user)
    }
    fun updateUser(user: User)
    {
        mSudokuApplicationHelper.updateUser(user)
    }
    fun findGameInfoById(id: Long) = mSudokuApplicationHelper.findGameInfoById(id)

}