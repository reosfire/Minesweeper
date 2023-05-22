package ru.reosfire.minesweeper.viewmodels

import android.util.Log
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
    private val maxMinesData = MutableStateFlow(Int.MAX_VALUE)

    val height: StateFlow<Int> = heightData
    val width: StateFlow<Int> = widthData
    val mines: StateFlow<Int> = minesData
    val maxMines: StateFlow<Int> = maxMinesData

    init {
        viewModelScope.launch {
            maxMinesData.collect {
                if (mines.value > it) minesData.emit(it)
                Log.d("Flows", "maxMines: $it   ${mines.value}")
            }
        }

        viewModelScope.launch {
            height.collect{
                maxMinesData.emit(height.value * width.value / 5 - 2)
                Log.d("Flows", "height: $it   ${width.value}")
            }
        }
        viewModelScope.launch {
            width.collect{
                maxMinesData.emit(height.value * width.value / 5 - 2)
                Log.d("Flows", "width: $it   ${height.value}")
            }
        }
    }

    fun setHeight(value: Int) {
        viewModelScope.launch {
            heightData.emit(value)
            Log.e("Flows emit", "height: $value")
        }
    }
    fun setWidth(value: Int) {
        viewModelScope.launch {
            widthData.emit(value)
            Log.e("Flows emit", "width: $value")
        }
    }

    fun setMines(value: Int) {
        viewModelScope.launch {
            minesData.emit(value)
            Log.e("Flows emit", "mines: $value")
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