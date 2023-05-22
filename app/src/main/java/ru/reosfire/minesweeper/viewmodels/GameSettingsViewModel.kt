package ru.reosfire.minesweeper.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.reosfire.minesweeper.game.GameSettings

class GameSettingsViewModel(application: Application): AndroidViewModel(application) {
    companion object {
        private const val SETTINGS_KEY = "game_settings"
        private const val DEFAULT_HEIGHT = 15
        private const val DEFAULT_WIDTH = 10
        private const val DEFAULT_MINES = 10
    }

    private val preferences: SharedPreferences

    private val heightData = MutableStateFlow(DEFAULT_HEIGHT)
    private val widthData = MutableStateFlow(DEFAULT_WIDTH)
    private val minesData = MutableStateFlow(DEFAULT_MINES)
    private val maxMinesData = MutableStateFlow(Int.MAX_VALUE)

    val height: StateFlow<Int> = heightData
    val width: StateFlow<Int> = widthData
    val mines: StateFlow<Int> = minesData
    val maxMines: StateFlow<Int> = maxMinesData

    init {
        preferences = application.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
        setSettings(GameSettings.fromSharedPrefs(preferences, SETTINGS_KEY, getSettings()))

        viewModelScope.launch {
            maxMinesData.collect {
                if (mines.value > it) minesData.emit(it)
            }
        }

        viewModelScope.launch {
            height.collect{
                maxMinesData.emit(height.value * width.value / 5 - 2)
            }
        }
        viewModelScope.launch {
            width.collect{
                maxMinesData.emit(height.value * width.value / 5 - 2)
            }
        }
    }

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

    fun saveToPreferences() {
        getSettings().saveToSharedPreferences(preferences, SETTINGS_KEY)
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