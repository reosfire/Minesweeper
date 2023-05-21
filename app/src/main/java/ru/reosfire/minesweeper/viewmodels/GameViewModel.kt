package ru.reosfire.minesweeper.viewmodels

import androidx.lifecycle.ViewModel
import ru.reosfire.minesweeper.game.Game

class GameViewModel: ViewModel() {
    var game: Game? = null
}