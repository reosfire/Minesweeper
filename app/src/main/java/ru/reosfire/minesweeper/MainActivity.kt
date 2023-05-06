package ru.reosfire.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.reosfire.minesweeper.databinding.ActivityMainBinding
import ru.reosfire.minesweeper.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, MainFragment())
            .commit()
    }
}