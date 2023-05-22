package ru.reosfire.minesweeper.room.migrations

import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.reosfire.minesweeper.game.GameResult

@RenameColumn("game_state", "completed", "result")
class Migration1_2: AutoMigrationSpec {
    override fun onPostMigrate(db: SupportSQLiteDatabase) {
        db.query("SELECT * FROM game_state").use {
            val idColumnIndex = it.getColumnIndex("id")
            val resultColumnIndex = it.getColumnIndex("result")

            while (it.moveToNext()) {
                val id = it.getInt(idColumnIndex)
                val completed = it.getInt(resultColumnIndex)

                val result = if (completed == 1) GameResult.Win else GameResult.Pending

                db.update("game_state",
                    SQLiteDatabase.CONFLICT_NONE,
                    contentValuesOf(
                        "result" to result
                    ),
                    "id = ?",
                    arrayOf(id)
                )
            }
        }
    }
}