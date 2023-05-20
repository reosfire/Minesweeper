package ru.reosfire.minesweeper.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.reosfire.minesweeper.GamesDatabase
import ru.reosfire.minesweeper.R
import ru.reosfire.minesweeper.SavedGamesAdapter
import ru.reosfire.minesweeper.databinding.FragmentMainBinding
import ru.reosfire.minesweeper.game.GameSettings
import ru.reosfire.minesweeper.views.fragments.dialogs.GameSettingsDialog

class MainFragment: Fragment() {
    companion object {
        private const val SETTINGS_KEY = "game_settings"
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var settings: GameSettings
    private var preferences: SharedPreferences? = null
    private lateinit var db: GamesDatabase
    private val gamesAdapter = SavedGamesAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settings = savedInstanceState?.getParcelable(SETTINGS_KEY) ?: GameSettings(10, 15, 20)

        preferences = context?.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
        if(preferences != null) settings = GameSettings.fromSharedPrefs(preferences!!, SETTINGS_KEY, settings)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.newGameButton.setOnClickListener {
            startGameFragment(GameFragment.create(settings))
        }

        binding.gameSettingsButton.setOnClickListener {
            val dialog = GameSettingsDialog.create(settings)
            dialog.setResultListener {
                settings = it
                if (preferences != null) it.saveToSharedPreferences(preferences!!, SETTINGS_KEY)
            }
            dialog.show(childFragmentManager, "TAGTAG")
        }

        gamesAdapter.setItemClickListener {
            startGameFragment(GameFragment.create(it))
        }

        binding.gamesList.adapter = gamesAdapter

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        db = Room.databaseBuilder(context,
            GamesDatabase::class.java, "games_database"
        ).build()

        MainScope().launch {
            withContext(Dispatchers.IO) {
                for (gameState in db.games.getAll()) {
                    MainScope().launch {
                        gamesAdapter.add(gameState)
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        db.close()
    }

    private fun startGameFragment(gameFragment: GameFragment) {
        gameFragment.setGameEndListener {
            gamesAdapter.add(it)
            lifecycleScope.launchWhenCreated {
                db.games.add(it)
            }
        }

        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainer, gameFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SETTINGS_KEY, settings)
    }
}