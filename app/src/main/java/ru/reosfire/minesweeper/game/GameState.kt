package ru.reosfire.minesweeper.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

@Parcelize
data class GameState(
    val height: Int,
    val width: Int,
    val minesJson: String,
    val fieldJson: String,
    val creationTime: Long,
    val time: Int,
    val completed: Boolean
): Parcelable {
    fun getSettings(): GameSettings {
        return GameSettings(width, height, getMinesCount())
    }

    fun getMinesCount(): Int {
        return Json.decodeFromString<JsonArray>(minesJson).size
    }
}