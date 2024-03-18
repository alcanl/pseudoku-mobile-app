package com.alcanl.sudoku.repository.entity.level

import androidx.compose.ui.text.intl.Locale
import com.alcanl.sudoku.global.EASY_LEVEL_COUNT
import com.alcanl.sudoku.global.EASY_TR
import com.alcanl.sudoku.global.HARD_LEVEL_COUNT
import com.alcanl.sudoku.global.HARD_TR
import com.alcanl.sudoku.global.MEDIUM_LEVEL_COUNT
import com.alcanl.sudoku.global.MEDIUM_TR
import com.alcanl.sudoku.global.TURKISH

enum class Level(value: Int, levelTR: String) {
    EASY(EASY_LEVEL_COUNT, EASY_TR), MEDIUM(MEDIUM_LEVEL_COUNT, MEDIUM_TR), HARD(HARD_LEVEL_COUNT, HARD_TR);
    private val mValue = value
    private val mLevelTr = levelTR
    fun getValue() : Int
    {
        return mValue
    }
    override fun toString(): String {
        return if (Locale.current.language.equals(TURKISH, true)) mLevelTr
            else this.name[0] + this.name.substring(1).lowercase()
    }
}