package com.alcanl.sudoku.global.theme

enum class BoardTheme(private val mValue: String) {
    THEME_DEFAULT("buttonDefault"), THEME_DARK("buttonDark"), THEME_LIGHT("buttonLight");

    override fun toString(): String = mValue
}