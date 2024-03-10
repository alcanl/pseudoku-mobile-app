package com.alcanl.sudoku.entity.level

enum class Level(value: Int) {
    EASY(32), MEDIUM(25), HARD(18);
    private val mValue = value
    fun getValue() : Int
    {
        return mValue
    }
}