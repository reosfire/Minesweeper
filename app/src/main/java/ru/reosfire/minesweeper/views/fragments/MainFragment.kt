package ru.reosfire.minesweeper.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.reosfire.minesweeper.R
import ru.reosfire.minesweeper.databinding.FragmentMainBinding
import ru.reosfire.minesweeper.game.GameSettings
import ru.reosfire.minesweeper.room.GamesDatabase
import ru.reosfire.minesweeper.viewmodels.GameSettingsViewModel
import ru.reosfire.minesweeper.viewmodels.GamesListViewModel
import ru.reosfire.minesweeper.views.adapters.SavedGamesAdapter
import ru.reosfire.minesweeper.views.fragments.dialogs.GameSettingsDialog

class MainFragment: Fragment() {
    companion object {
        private const val SETTINGS_KEY = "game_settings"
    }

    private val viewModel by viewModels<GamesListViewModel>()
    private val settingsViewModel by activityViewModels<GameSettingsViewModel>()
    private lateinit var binding: FragmentMainBinding
    private var preferences: SharedPreferences? = null
    private lateinit var db: GamesDatabase
    private val gamesAdapter = SavedGamesAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = context?.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
        if(preferences != null) settingsViewModel.setSettings(GameSettings.fromSharedPrefs(preferences!!, SETTINGS_KEY, settingsViewModel.getSettings()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.newGameButton.setOnClickListener {
            startGameFragment(GameFragment.create(settingsViewModel.getSettings()))
        }

        binding.gameSettingsButton.setOnClickListener {
            val dialog = GameSettingsDialog()
            dialog.show(childFragmentManager, "TAGTAG")
        }

        gamesAdapter.setItemClickListener {
            if (it.completed) return@setItemClickListener
            startGameFragment(GameFragment.create(it))
        }

        with(binding.gamesList) {
            adapter = gamesAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

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
            lifecycleScope.launchWhenCreated {
                db.games.add(it)
                withContext(Dispatchers.IO) {
                    val data = db.games.getAll()
                    MainScope().launch {
                        gamesAdapter.set(data)
                    }
                }
            }
        }

        parentFragmentManager.commit {
            addToBackStack(null)
            replace(R.id.fragmentContainer, gameFragment)
        }
    }
}