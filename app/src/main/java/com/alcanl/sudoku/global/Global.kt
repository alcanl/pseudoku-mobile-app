package com.alcanl.sudoku.global

const val TURKISH = "tr"
const val EASY_TR = "Kolay"
const val MEDIUM_TR = "Orta"
const val HARD_TR = "Zor"
const val ENGLISH_TAG = "en"
const val EASY_LEVEL_COUNT = 37
const val MEDIUM_LEVEL_COUNT = 32
const val HARD_LEVEL_COUNT = 27

fun Array<IntArray>.transpose() : Array<IntArray> {

    val t = Array(this.size) { IntArray(this[0].size) }

    for (i in this.indices)
        for (j in this[i].indices)
            t[j][i] = this[i][j]

    return t
}
