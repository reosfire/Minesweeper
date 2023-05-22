package ru.reosfire.minesweeper.room

import androidx.room.TypeConverter
import ru.reosfire.minesweeper.game.GameResult

class Converters {
    @TypeConverter
    fun fromGameResult(result: GameResult) = result.name
    @TypeConverter
    fun toGameResult(value: String) = GameResult.valueOf(value)
}