package ru.reosfire.minesweeper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import ru.reosfire.minesweeper.databinding.FragmentGameBinding
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
    private lateinit var timerJob: Job
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

        binding.flags.text = "${game.getFlags()}/${settings.minesCount}"

        runTimer()

        binding.gameField.setCellClickedListener { x, y ->
            if (binding.flagCheckbox.isChecked) {
                game.toggleFlag(y, x)
                binding.flags.text = "${game.getFlags()}/${settings.minesCount}"
            }
            else {
                val openResult = game.open(y, x)
                if (openResult == OpenResult.Loose) {
                    stopTimer()
                    setupGame(settings)
                    Toast.makeText(context, "LOOOSSSEE!!!", Toast.LENGTH_SHORT).show()
                }
                else if (openResult == OpenResult.Win){
                    stopTimer()
                    setupGame(settings)
                    Toast.makeText(context, "WWWIIIIIIINNN!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob.cancel()
    }
    private fun runTimer() {
        var seconds = 0
        timerJob = lifecycleScope.launchWhenCreated {
            while (isActive) {
                binding.timer.text = seconds.toString()
                seconds++
                delay(1000)
            }
        }
    }
}