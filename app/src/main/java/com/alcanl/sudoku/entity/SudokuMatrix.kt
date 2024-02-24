package com.alcanl.sudoku.entity

import com.alcanl.sudoku.di.module.generator.GeneratorModule

class SudokuMatrix {
    private lateinit var matrix: Array<IntArray>
    init {
        setNewMatrix()
    }
    fun checkValue(value: Int, index: Int) = matrix[index / 10][index % 10] == value
    private fun setNewMatrix()
    {
        matrix = GeneratorModule.generate()
    }
    fun getValue(index: Int) : Int = matrix[index / 10][index % 10]
}