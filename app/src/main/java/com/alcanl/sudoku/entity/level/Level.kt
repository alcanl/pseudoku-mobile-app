package com.alcanl.sudoku.entity.level

enum class Level(value: Int) {
    EASY(35), MEDIUM(30), HARD(25);
    private val mValue = value
    fun getValue() : Int
    {
        return mValue
    }
}