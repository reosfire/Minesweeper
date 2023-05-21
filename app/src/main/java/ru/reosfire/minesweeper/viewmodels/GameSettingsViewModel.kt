package ru.reosfire.minesweeper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.reosfire.minesweeper.game.GameSettings

class GameSettingsViewModel: ViewModel() {
    companion object {
        private const val DEFAULT_HEIGHT = 15
        private const val DEFAULT_WIDTH = 10
        private const val DEFAULT_MINES = 10
    }

    private val heightData = MutableStateFlow(DEFAULT_HEIGHT)
    private val widthData = MutableStateFlow(DEFAULT_WIDTH)
    private val minesData = MutableStateFlow(DEFAULT_MINES)

    val height: StateFlow<Int> = heightData
    val width: StateFlow<Int> = widthData
    val mines: StateFlow<Int> = minesData

    fun setHeight(value: Int) {
        viewModelScope.launch {
            heightData.emit(value)
        }
    }
    fun setWidth(value: Int) {
        viewModelScope.launch {
            widthData.emit(value)
        }
    }

    fun setMines(value: Int) {
        viewModelScope.launch {
            minesData.emit(value)
        }
    }

    fun setSettings(settings: GameSettings) {
        setHeight(settings.height)
        setWidth(settings.width)
        setMines(settings.minesCount)
    }

    fun getSettings(): GameSettings {
        return GameSettings(width.value, height.value, mines.value)
    }
}