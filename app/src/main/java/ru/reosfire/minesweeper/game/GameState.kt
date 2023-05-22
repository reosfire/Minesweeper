package ru.reosfire.minesweeper.game

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

@Parcelize
@Entity(tableName = "game_state")
data class GameState(
    val height: Int,
    val width: Int,
    val minesJson: String,
    val fieldJson: String,
    val creationTime: Long,
    val time: Int,
    val result: GameResult,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Parcelable {
    fun getSettings(): GameSettings {
        return GameSettings(width, height, getMinesCount())
    }

    fun getMinesCount(): Int {
        return Json.decodeFromString<JsonArray>(minesJson).size
    }
}