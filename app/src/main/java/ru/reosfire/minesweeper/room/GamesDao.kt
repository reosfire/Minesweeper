package ru.reosfire.minesweeper.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.reosfire.minesweeper.game.GameState

@Dao
interface GamesDao {
    @Upsert
    suspend fun add(game: GameState)

    @Query("SELECT * FROM game_state ORDER BY creationTime DESC")
    fun getAll(): List<GameState>
}