package ru.reosfire.minesweeper.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val width: Int,
    val height: Int,
    val minesCount: Int
) : Parcelable