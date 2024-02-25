package com.alcanl.sudoku.entity

import com.alcanl.sudoku.di.module.generator.GeneratorModule
import kotlin.random.Random

class SudokuMatrix {
    private lateinit var mSolvedMatrix: Array<IntArray>
    private lateinit var mUnSolvedMatrix: Array<IntArray>

    init {
        generateNewMatrix()
        prepareUnSolvedMatrix()
    }
    fun checkValue(value: Int, index: Int) = mSolvedMatrix[index / 10][index % 10] == value
    fun generateNewMatrix()
    {
        mSolvedMatrix = GeneratorModule.generate()
        prepareUnSolvedMatrix()
    }
    fun getValue(index: Int) : String
    {
        return if (mUnSolvedMatrix[index / 10][index % 10] == 0) "" else mUnSolvedMatrix[index / 10][index % 10].toString()
    }
    private fun prepareUnSolvedMatrix()
    {
       /* val boolMatrix = Array(9) { BooleanArray(9)}
        val random = Random

        boolMatrix.forEach { it.fill(random.nextBoolean()) } */

        mUnSolvedMatrix = mSolvedMatrix
    }
}