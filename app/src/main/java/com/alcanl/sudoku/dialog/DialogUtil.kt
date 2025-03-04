package com.alcanl.sudoku.dialog

import androidx.core.content.ContextCompat
import com.alcanl.android.app.sudoku.R
import com.alcanl.sudoku.MainActivity
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.background
import com.example.awesomedialog.body
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.position
import com.example.awesomedialog.title

fun showLoseAndPlayAgainDialog(activity: MainActivity, onPositive: () -> Unit, onNegative: () -> Unit)
{
    AwesomeDialog.build(activity)
        .position(AwesomeDialog.POSITIONS.CENTER)
        .body(body = "Sorry, You made 3 mistakes and nothing left.\n Do you want to play again?", color = R.color.theme_blue)
        .background(androidx.cardview.R.color.cardview_light_background)
        .title(title = "Game Over", titleColor = R.color.theme_blue)
        .onPositive(text = "Yes", textColor = ContextCompat.getColor(activity, android.R.color.white),
            buttonBackgroundColor = R.color.theme_blue) { onPositive() }
        .onNegative(text = "No", textColor = ContextCompat.getColor(activity, android.R.color.white),
            buttonBackgroundColor = R.color.theme_blue) { onNegative() }
        .show()
}

fun showWinAndPlayAgainDialog(activity: MainActivity, onPositive: () -> Unit, onNegative: () -> Unit)
{
    AwesomeDialog.build(activity)
        .position(AwesomeDialog.POSITIONS.CENTER)
        .body(body = "Do you want to play a new game?", color = R.color.theme_blue)
        .background(androidx.cardview.R.color.cardview_light_background)
        .title(title = "Congratulations, Its a win!", titleColor = R.color.theme_blue)
        .onPositive(text = "Yes", textColor = ContextCompat.getColor(activity, android.R.color.white),
            buttonBackgroundColor = R.color.theme_blue) { onPositive() }
        .onNegative(text = "No", textColor = ContextCompat.getColor(activity, android.R.color.white),
            buttonBackgroundColor = R.color.theme_blue) { onNegative() }
        .show()
}
