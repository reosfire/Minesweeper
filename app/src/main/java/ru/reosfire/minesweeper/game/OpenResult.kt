package ru.reosfire.minesweeper.game

enum class OpenResult {
    Win,
    Loose,
    Updated, //Opened some numbers
    NothingChanged //Open flag
}