package ru.reosfire.minesweeper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.reosfire.minesweeper.databinding.SavedGameItemBinding
import ru.reosfire.minesweeper.game.GameState

class SavedGamesAdapter(private val games: MutableList<GameState>): RecyclerView.Adapter<SavedGamesAdapter.SavedGameViewHolder>() {
    fun add(game: GameState) {
        games.add(game)
        notifyDataSetChanged()
        //notifyItemInserted(games.lastIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedGameViewHolder {
        return with(LayoutInflater.from(parent.context)) {
            SavedGameViewHolder(SavedGameItemBinding.inflate(this, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(holder: SavedGameViewHolder, position: Int) {
        holder.setText(games[position].field)
    }

    class SavedGameViewHolder(private val binding: SavedGameItemBinding): ViewHolder(binding.root) {
        fun setText(value: String) {
            binding.createdAtText.text = value
        }
    }
}