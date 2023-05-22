package ru.reosfire.minesweeper.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.reosfire.minesweeper.game.GameState

@Database(
    entities = [GameState::class],
    version = 1,
    exportSchema = true
)
abstract class GamesDatabase: RoomDatabase() {
    abstract val games: GamesDao
}