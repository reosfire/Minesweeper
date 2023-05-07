package ru.reosfire.minesweeper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.reosfire.minesweeper.databinding.FragmentGameBinding
import ru.reosfire.minesweeper.field.cells.EmptyCell
import ru.reosfire.minesweeper.field.cells.FlagCell
import ru.reosfire.minesweeper.game.Game
import ru.reosfire.minesweeper.game.GameSettings
import ru.reosfire.minesweeper.game.OpenResult

class GameFragment: Fragment() {
    companion object {
        private const val SETTINGS_KEY = "game_settings"

        fun create(settings: GameSettings): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SETTINGS_KEY, settings)
                }
            }
        }
    }

    private lateinit var binding: FragmentGameBinding
    private lateinit var game: Game
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val settings = arguments?.getParcelable<GameSettings>(SETTINGS_KEY)
        if (settings != null) setupGame(settings)

        return binding.root
    }

    private fun setupGame(settings: GameSettings) {
        game = Game(settings)
        binding.gameField.gameField = game.getField()

        binding.gameField.setCellClickedListener { x, y ->
            if (game.getField().get(y, x) is FlagCell && binding.flagCheckbox.isChecked) {
                game.getField().set(y, x, EmptyCell())
            }
            else if (game.getField().get(y, x) is EmptyCell && binding.flagCheckbox.isChecked) {
                game.getField().set(y, x, FlagCell())
            }
            else if (!binding.flagCheckbox.isChecked && game.getField().get(y, x) !is FlagCell) {
                val openResult = game.open(y, x)
                if (openResult == OpenResult.Loose) {
                    setupGame(settings)
                    Toast.makeText(context, "LOOOSSSEE!!!", Toast.LENGTH_LONG).show()
                }
                else if (openResult == OpenResult.Win){
                    setupGame(settings)
                    Toast.makeText(context, "WWWIIIIIIINNN!!!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}