package ru.reosfire.minesweeper.game

enum class GameResult {
    Win,
    Loose,
    Pending;

    val completed
        get() = this == Win || this == Loose
}