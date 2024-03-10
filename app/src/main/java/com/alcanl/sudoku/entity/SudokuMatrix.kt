package com.alcanl.sudoku.entity

import com.alcanl.sudoku.entity.generator.SudokuGenerator
import com.alcanl.sudoku.entity.level.Level
import kotlin.random.Random

class SudokuMatrix {
    private lateinit var mSolvedMatrix: Array<IntArray>
    private val mUnSolvedMatrix: Array<IntArray>
    private lateinit var mLevel : Level

    init {
        setLeveL(Level.HARD)
        mUnSolvedMatrix = Array(9) { IntArray(9) }
        generateNewMatrix()
        prepareUnSolvedMatrix()
    }
    fun setLeveL(level: Level = Level.EASY)
    {
        mLevel = level
    }
    fun checkValue(value: Int, index: Int) = mSolvedMatrix[index / 10][index % 10] == value
    fun generateNewMatrix()
    {
        mSolvedMatrix = SudokuGenerator.generate()
        prepareUnSolvedMatrix()
    }
    fun getValue(index: Int) : String
    {
       return if (mUnSolvedMatrix[index / 10][index % 10] == 0) "" else mUnSolvedMatrix[index / 10][index % 10].toString()
    }
    private fun prepareUnSolvedMatrix()
    {
        val random = Random
        var index1 : Int
        var index2 : Int
        val unSolvedMap = HashMap<Int,Int>()

        while (unSolvedMap.size != mLevel.getValue()) {
            index1 = random.nextInt(0, 9)
            index2 = random.nextInt(0, 9)
            if (!unSolvedMap.containsKey(index1))
                unSolvedMap[index1 * 10 + index2] = mSolvedMatrix[index1][index2]
        }

        unSolvedMap.keys.forEach { mUnSolvedMatrix[it / 10][it % 10] = unSolvedMap[it]!! }
    }
}