package ru.reosfire.minesweeper

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.reosfire.minesweeper.game.GameState

@Dao
interface GamesDao {
    @Insert
    suspend fun add(game: GameState)

    @Query("SELECT * FROM game_state")
    fun getAll(): List<GameState>
}