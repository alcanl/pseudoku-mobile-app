package com.alcanl.sudoku.global

enum class BoardTheme(private val mValue: String) {
    THEME_DEFAULT("buttonDefault"), THEME_DARK("buttonDark"), THEME_LIGHT("buttonLight");

    override fun toString(): String = mValue
}