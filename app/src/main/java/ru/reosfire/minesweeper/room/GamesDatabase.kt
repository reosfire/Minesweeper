package ru.reosfire.minesweeper.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.reosfire.minesweeper.game.GameState

@Database(
    entities = [GameState::class],
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = Migration1_2::class
        )
    ]
)
abstract class GamesDatabase: RoomDatabase() {
    abstract val games: GamesDao
}