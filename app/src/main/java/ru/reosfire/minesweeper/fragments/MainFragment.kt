package ru.reosfire.minesweeper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.reosfire.minesweeper.R
import ru.reosfire.minesweeper.databinding.FragmentMainBinding
import ru.reosfire.minesweeper.fragments.dialogs.GameSettingsDialog
import ru.reosfire.minesweeper.game.GameSettings

class MainFragment: Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var settings: GameSettings

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        settings = GameSettings(10, 15, 20)

        binding.newGameButton.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.fragmentContainer, GameFragment.create(settings))
            }
        }

        binding.gameSettingsButton.setOnClickListener {
            val dialog = GameSettingsDialog.create(settings)
            dialog.setResultListener {
                settings = it
            }
            dialog.show(childFragmentManager, "TAGTAG")
        }

        return binding.root
    }
}