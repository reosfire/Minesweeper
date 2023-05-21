package ru.reosfire.minesweeper.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.reosfire.minesweeper.databinding.SavedGameItemBinding
import ru.reosfire.minesweeper.game.GameState
import java.text.DateFormat
import java.util.*
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
        with(games[position]) {
            holder.setTime(time)
            holder.setSize(height, width)
            holder.setCreatedAtTime(creationTime)
        }
        holder.setOnClickListener {
            itemClickListener?.invoke(games[position])
        }
    }

    class SavedGameViewHolder(private val binding: SavedGameItemBinding): ViewHolder(binding.root) {
        fun setTime(time: Int) {
            binding.time.text = time.toDuration(DurationUnit.SECONDS).toString()
        }
        fun setCreatedAtTime(time: Long) {
            val formatter = DateFormat.getDateTimeInstance()
            binding.creationTime.text = formatter.format(Date(time))
        }
        fun setSize(h: Int, w: Int) {
            binding.size.text = "${h}x${w}"
        }

        fun setOnClickListener(listener: View.OnClickListener) {
            binding.root.setOnClickListener(listener)
        }
    }
}