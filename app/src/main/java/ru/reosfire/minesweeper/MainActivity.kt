package ru.reosfire.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.reosfire.minesweeper.databinding.ActivityMainBinding
import ru.reosfire.minesweeper.field.cells.EmptyCell
import ru.reosfire.minesweeper.field.cells.FlagCell
import ru.reosfire.minesweeper.field.cells.NumberCell
import ru.reosfire.minesweeper.game.Game
import ru.reosfire.minesweeper.game.GameSettings
import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gameField.game = Game(GameSettings(10, 11, 20))

        val rnd = Random()
        with(binding.gameField.game!!) {
            for (i in 0 until this.getField().height) {
                for (j in 0 until this.getField().width) {
                    when (rnd.nextInt(5)) {
                        0 -> getField().set(i, j, FlagCell())
                        1 -> getField().set(i, j, NumberCell(rnd.nextInt(9) + 1))
                        else -> getField().set(i, j, EmptyCell())
                    }
                }
            }
        }
    }
}