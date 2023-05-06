package ru.reosfire.minesweeper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.reosfire.minesweeper.databinding.FragmentGameBinding
import ru.reosfire.minesweeper.field.cells.EmptyCell
import ru.reosfire.minesweeper.field.cells.FlagCell
import ru.reosfire.minesweeper.game.Game
import ru.reosfire.minesweeper.game.GameSettings
import ru.reosfire.minesweeper.game.OpenResult

class GameFragment: Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var game: Game
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        setupGame()

        return binding.root
    }

    private fun setupGame() {
        game = Game(GameSettings(10, 15, 20))
        binding.gameField.gameField = game.getField()

        binding.gameField.setCellClickedListener { x, y ->
            if (game.getField().get(y, x) is FlagCell && binding.flagCheckbox.isChecked) {
                game.getField().set(y, x, EmptyCell())
            }
            else if (game.getField().get(y, x) is EmptyCell && binding.flagCheckbox.isChecked) {
                game.getField().set(y, x, FlagCell())
            }
            else if (!binding.flagCheckbox.isChecked && game.getField().get(y, x) !is FlagCell) {
                if (game.open(y, x) == OpenResult.Loose) setupGame()
            }
        }
    }
}