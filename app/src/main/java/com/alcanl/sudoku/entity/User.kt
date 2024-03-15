package com.alcanl.sudoku.entity

data class User(var name: String, var surName: String, var level: Int, var totalSuccess: Int,
    var totalGame: Int, var eMail: String, var bestScore:Int) {
    override fun toString(): String {
        return String.format("$name $surName")
    }
}
