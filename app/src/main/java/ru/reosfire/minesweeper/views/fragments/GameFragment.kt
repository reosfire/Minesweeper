package ru.reosfire.minesweeper.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.reosfire.minesweeper.databinding.FragmentGameBinding
import ru.reosfire.minesweeper.game.Game
import ru.reosfire.minesweeper.game.GameSettings
import ru.reosfire.minesweeper.game.GameState
import ru.reosfire.minesweeper.game.OpenResult
import kotlin.time.DurationUnit
import kotlin.time.toDuration

typealias GameEndListener = (GameState) -> Unit

class GameFragment: Fragment() {
    companion object {
        private const val SETTINGS_KEY = "game_settings"
        private const val INIT_STATE_KEY = "init_state"

        fun create(settings: GameSettings): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SETTINGS_KEY, settings)
                }
            }
        }
        fun create(state: GameState): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(INIT_STATE_KEY, state)
                }
            }
        }
    }

    private lateinit var binding: FragmentGameBinding
    private var gameEndListener: GameEndListener? = null
    private var game: Game? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            if (game != null && game!!.started) gameEndListener?.invoke(game!!.getState())
            parentFragmentManager.popBackStack()
        }

        val settings = arguments?.getParcelable<GameSettings>(SETTINGS_KEY)
        val initState = arguments?.getParcelable<GameState>(INIT_STATE_KEY)
        if (settings != null) setupGame(settings)
        else if (initState != null) setupGame(initState)
        else throw IllegalStateException("Game settings and state are both not set in bundle")

        return binding.root
    }

    fun setGameEndListener(listener: GameEndListener) {
        gameEndListener = listener
    }


    private fun setupGame(settings: GameSettings) {
        game = Game(settings)
        setupGame(game!!)
    }
    private fun setupGame(state: GameState) {
        game = Game(state)
        setupGame(game!!)
    }
    private fun setupGame(game: Game) {
        binding.gameField.gameField = game.field

        game.timer.seconds.observe(viewLifecycleOwner) {
            binding.timer.text = it.toDuration(DurationUnit.SECONDS).toString()
        }

        binding.flags.text = "${game.flags}/${game.settings.minesCount}"

        binding.gameField.setCellClickedListener { x, y ->
            if (binding.flagCheckbox.isChecked) {
                game.toggleFlag(y, x)
                binding.flags.text = "${game.flags}/${game.settings.minesCount}"
            }
            else {
                val openResult = game.open(y, x)
                if (openResult == OpenResult.Loose) {
                    gameEndListener?.invoke(game.getState())
                    setupGame(game.settings)
                    Toast.makeText(context, "LOOOSSSEE!!!", Toast.LENGTH_SHORT).show()
                }
                else if (openResult == OpenResult.Win){
                    gameEndListener?.invoke(game.getState())
                    setupGame(game.settings)
                    Toast.makeText(context, "WWWIIIIIIINNN!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}