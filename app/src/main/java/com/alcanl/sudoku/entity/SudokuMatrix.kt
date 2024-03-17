package com.alcanl.sudoku.entity

import com.alcanl.sudoku.entity.generator.SudokuGenerator
import com.alcanl.sudoku.entity.level.Level
import kotlin.random.Random

class SudokuMatrix {

    private lateinit var mSolvingMatrix : Array<IntArray>
    private lateinit var mSolvedMatrix: Array<IntArray>
    private lateinit var mUnSolvedMatrix : Array<IntArray>
    private lateinit var mLevel : Level
    private val mCounterArray : IntArray = IntArray(9)
    init {
        setLeveL()
        generateNewMatrix()
    }
    private fun isCompletedCallback(intArray: IntArray) = intArray.none { it == 0 }
    fun setLeveL(level: Level = Level.EASY)
    {
        mLevel = level
    }
    fun isTrueValue(value: Int, index: Int) = mSolvedMatrix[index / 10][index % 10] == value
    fun isAvailableValueCountOver(value: Int) = mCounterArray[value - 1] == 0
    fun generateNewMatrix()
    {
        mSolvedMatrix = SudokuGenerator.generate()
        prepareUnSolvedMatrix()
        prepareSolvingMatrix()
        mCounterArray.fill(9)
        calculateNumberCounts()

    }
    fun getValue(index: Int) : String
    {
       return if (mSolvingMatrix[index / 10][index % 10] == 0) "" else mSolvingMatrix[index / 10][index % 10].toString()
    }
    fun decreaseNumberCount(number: Int)
    {
        --mCounterArray[number - 1]
    }
    fun increaseNumberCount(number: Int)
    {
        ++mCounterArray[number - 1]
    }
    fun getHint() : Pair<Int, String>
    {
        val random = Random
        var index : Int
        var value : Int

        do {
            index = random.nextInt(1,9) * 10 + random.nextInt(1, 9)
            value = mSolvingMatrix[index / 10][index % 10]
        } while (value != 0)

        return Pair(index, mSolvedMatrix[index / 10][index % 10].toString())
    }
    private fun calculateNumberCounts()
    {
        mSolvingMatrix.forEach(this::calculateNumberCountsCallback)
    }
    private fun calculateNumberCountsCallback(index: IntArray)
    {
        index.filter {
            it != 0 }.forEach { --mCounterArray[it - 1] }
    }
    private fun prepareUnSolvedMatrix()
    {
        mUnSolvedMatrix = Array(9) { IntArray(9) }
        val random = Random
        var index1 : Int
        var index2 : Int
        val unSolvedMap = HashMap<Int,Int>()

        while (unSolvedMap.size != mLevel.getValue()) {
            index1 = random.nextInt(0, 9)
            index2 = random.nextInt(0, 9)
            if (!unSolvedMap.containsKey(index1 * 10 + index2))
                unSolvedMap[index1 * 10 + index2] = mSolvedMatrix[index1][index2]
        }

        unSolvedMap.keys.forEach { mUnSolvedMatrix[it / 10][it % 10] = unSolvedMap[it]!! }
    }
    private fun prepareSolvingMatrix()
    {
        mSolvingMatrix = Array(9) { IntArray(9) }
        var index = 0
        mUnSolvedMatrix.forEach { it.copyInto(mSolvingMatrix[index++]) }
    }
    fun setCell(index: Int, value: Int)
    {
        mSolvingMatrix[index / 10][index % 10] = value
    }
    fun clearCell(index: Int)
    {
        setCell(index, 0)
    }
    fun isCompleted() : Boolean
    {
        return mSolvingMatrix.none { !isCompletedCallback(it) }
    }
    fun resetCurrentMatrix()
    {
        prepareSolvingMatrix()
        calculateNumberCounts()
    }

}