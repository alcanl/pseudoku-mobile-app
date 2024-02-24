package com.alcanl.android.app.sudoku.global

fun Array<IntArray>.transpose() : Array<IntArray> {

    val t = Array(this.size) { IntArray(this[0].size) }

    for (i in this.indices)
        for (j in this[i].indices)
            t[j][i] = this[i][j]

    return t
}