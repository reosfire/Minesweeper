package ru.reosfire.minesweeper

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.reosfire.minesweeper.game.GameState

@Database(
    entities = [GameState::class],
    version = 1
)
abstract class GamesDatabase: RoomDatabase() {
    abstract val games: GamesDao
}