package ru.reosfire.minesweeper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.reosfire.minesweeper.databinding.FragmentGameBinding
import ru.reosfire.minesweeper.field.cells.EmptyCell
import ru.reosfire.minesweeper.field.cells.FlagCell
import ru.reosfire.minesweeper.field.cells.NumberCell
import ru.reosfire.minesweeper.game.Game
import ru.reosfire.minesweeper.game.GameSettings
import java.util.Random

class GameFragment: Fragment() {
    private lateinit var binding: FragmentGameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.gameField.game = Game(GameSettings(10, 5, 20))

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

        return binding.root
    }
}