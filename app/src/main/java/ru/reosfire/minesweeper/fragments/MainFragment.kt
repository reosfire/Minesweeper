package ru.reosfire.minesweeper.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.reosfire.minesweeper.R
import ru.reosfire.minesweeper.SavedGamesAdapter
import ru.reosfire.minesweeper.databinding.FragmentMainBinding
import ru.reosfire.minesweeper.fragments.dialogs.GameSettingsDialog
import ru.reosfire.minesweeper.game.GameSettings
import ru.reosfire.minesweeper.game.GameState

class MainFragment: Fragment() {
    companion object {
        private const val SETTINGS_KEY = "game_settings"
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var settings: GameSettings
    private var preferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        settings = savedInstanceState?.getParcelable(SETTINGS_KEY) ?: GameSettings(10, 15, 20)

        preferences = context?.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
        if(preferences != null) settings = GameSettings.fromSharedPrefs(preferences!!, SETTINGS_KEY, settings)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

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
                if (preferences != null) it.saveToSharedPreferences(preferences!!, SETTINGS_KEY)
            }
            dialog.show(childFragmentManager, "TAGTAG")
        }

        val gamesAdapter = SavedGamesAdapter(Array(30) { GameState(10, 10, "asdfasdfasdf", "asdf") }.toMutableList())
        binding.gamesList.adapter = gamesAdapter

        binding.statsButton.setOnClickListener {
            gamesAdapter.add(GameState(1,1,"123123asdf", "123123asdf2134124fa"))
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SETTINGS_KEY, settings)
    }
}