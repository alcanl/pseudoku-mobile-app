package com.alcanl.sudoku.di.module.generator

import java.lang.StringBuilder
object GeneratorModule {
    private var mSudoku = Array(9) { IntArray(9) }
    fun generate() : Array<IntArray>
    {
        fillSudoku()
        return mSudoku
    }

    private fun fillSudoku()
    {
        fillDiagonalRegions()
        solveSudoku()
        shuffleSudoku()
    }

    private fun fillDiagonalRegions()
    {
        val values = (1..9).toMutableList()
        values.shuffle()

        for (i in 0 until 9 step 3) {
            fillRegion(i, i, values)
        }
    }

    private fun fillRegion(startRow: Int, startCol: Int, values: MutableList<Int>)
    {
        var index = 0
        for (i in startRow until startRow + 3) {
            for (j in startCol until startCol + 3) {
                mSudoku[i][j] = values[index]
                index++
            }
        }
    }

    private fun shuffleSudoku()
    {

        for (i in 0 until 9 step 3) {
            val temp = mSudoku[i]
            mSudoku[i] = mSudoku[i + 1]
            mSudoku[i + 1] = mSudoku[i + 2]
            mSudoku[i + 2] = temp
        }

        for (j in 0 until 9 step 3) {
            for (i in 0 until 9) {
                val temp = mSudoku[i][j]
                mSudoku[i][j] = mSudoku[i][j + 1]
                mSudoku[i][j + 1] = mSudoku[i][j + 2]
                mSudoku[i][j + 2] = temp
            }
        }
    }

    private fun solveSudoku(): Boolean
    {
        val emptyCell = findEmptyCell() ?: return true

        val (row, col) = emptyCell
        for (num in 1..9) {
            if (isValidMove(row, col, num)) {
                mSudoku[row][col] = num

                if (solveSudoku()) {
                    return true
                }

                mSudoku[row][col] = 0
            }
        }

        return false
    }

    private fun isValidMove(row: Int, col: Int, num: Int): Boolean
    {

        for (i in 0 until 9) {
            if (mSudoku[row][i] == num || mSudoku[i][col] == num) {
                return false
            }
        }

        val startRow = row - row % 3
        val startCol = col - col % 3
        for (i in startRow until startRow + 3) {
            for (j in startCol until startCol + 3) {
                if (mSudoku[i][j] == num) {
                    return false
                }
            }
        }

        return true
    }
    private fun findEmptyCell(): Pair<Int, Int>?
    {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (mSudoku[i][j] == 0) {
                    return Pair(i, j)
                }
            }
        }
        return null
    }
    override fun toString() : String
    {
        val sb = StringBuilder()

        for (i in 0 until 9) {
            for (j in 0 until 9) {
                sb.append("${mSudoku[i][j]} ")
            }
            sb.append("\n")
        }

        return sb.toString()
    }
}