package ru.reosfire.minesweeper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.reosfire.minesweeper.databinding.SavedGameItemBinding
import ru.reosfire.minesweeper.game.GameState
import kotlin.time.DurationUnit
import kotlin.time.toDuration

typealias ItemClickListener = (GameState) -> Unit

class SavedGamesAdapter(private val games: MutableList<GameState>): RecyclerView.Adapter<SavedGamesAdapter.SavedGameViewHolder>() {
    private var itemClickListener: ItemClickListener? = null

    fun add(game: GameState) {
        games.add(game)
        notifyItemInserted(games.lastIndex)
    }

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
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

        holder.setText(games[position].time.toDuration(DurationUnit.SECONDS).toString())
        holder.setOnClickListener {
            itemClickListener?.invoke(games[position])
        }
    }

    class SavedGameViewHolder(private val binding: SavedGameItemBinding): ViewHolder(binding.root) {
        fun setText(value: String) {
            binding.createdAtText.text = value
        }
        fun setOnClickListener(listener: View.OnClickListener) {
            binding.root.setOnClickListener(listener)
        }
    }
}