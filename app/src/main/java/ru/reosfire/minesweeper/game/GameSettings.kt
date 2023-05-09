package ru.reosfire.minesweeper.game

import android.content.SharedPreferences
import android.os.Parcelable
import androidx.core.content.edit
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val width: Int,
    val height: Int,
    val minesCount: Int
) : Parcelable {
    companion object {
        private const val WIDTH_KEY = "width"
        private const val HEIGHT_KEY = "height"
        private const val MINES_COUNT_KEY = "mines_count"

        fun fromSharedPrefs(prefs: SharedPreferences, key: String, default: GameSettings): GameSettings = with(prefs) {
            GameSettings(
                getInt(key + "_" + WIDTH_KEY, default.width),
                getInt(key + "_" + HEIGHT_KEY, default.height),
                getInt(key + "_" + MINES_COUNT_KEY, default.minesCount))
        }
    }

    fun cellsCount(): Int {
        return width * height
    }

    fun saveToSharedPreferences(prefs: SharedPreferences, key: String) {
        prefs.edit {
            putInt(key + "_" + WIDTH_KEY, width)
            putInt(key + "_" + HEIGHT_KEY, height)
            putInt(key + "_" + MINES_COUNT_KEY, minesCount)
        }
    }
}