package ru.reosfire.minesweeper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.reosfire.minesweeper.R
import ru.reosfire.minesweeper.databinding.FragmentMainBinding
import ru.reosfire.minesweeper.game.GameSettings

class MainFragment: Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.newGameButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .addToBackStack("ABOBA")
                .add(R.id.fragmentContainer, GameFragment.create(GameSettings(10, 15, 20)))
                .commit()
        }

        return binding.root
    }
}