package ru.reosfire.minesweeper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.reosfire.minesweeper.databinding.ActivityMainBinding
import ru.reosfire.minesweeper.fragments.MainFragment
import ru.reosfire.minesweeper.game.GameState

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, MainFragment())
            .commit()

        val gamesAdapter = SavedGamesAdapter(Array(100) { GameState(10, 10, "asdfasdfasdf", "asdf") }.toMutableList())
        binding.gamesList.adapter = gamesAdapter
    }
}