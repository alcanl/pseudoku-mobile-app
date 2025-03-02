package com.alcanl.sudoku.dialog

import com.alcanl.android.app.sudoku.R
import com.alcanl.sudoku.MainActivity
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.body
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.title

fun showLoseAndPlayAgainDialog(activity: MainActivity, onPositive: () -> Unit, onNegative: () -> Unit)
{
    AwesomeDialog.build(activity)
        .body(body = "Sorry, You made 3 mistakes and nothing left.\n Do you want to play again?", color = R.color.colorCardDT)
        .title(title = "Game Over", titleColor = R.color.falseMove)
        .onPositive(text = "Yes", textColor = R.color.colorCardDT) { onPositive() }
        .onNegative(text = "No", textColor = R.color.colorCardDT) { onNegative() }
        .show()
}

fun showWinAndPlayAgainDialog(activity: MainActivity, onPositive: () -> Unit, onNegative: () -> Unit)
{
    AwesomeDialog.build(activity)
        .body(body = "Do you want to play a new game?", color = R.color.colorCardDT)
        .title(title = "Congratulations, Its a win!", titleColor = R.color.falseMove)
        .onPositive(text = "Yes", textColor = R.color.colorCardDT) { onPositive() }
        .onNegative(text = "No", textColor = R.color.colorCardDT) { onNegative() }
        .show()
}
