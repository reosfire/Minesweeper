package ru.reosfire.minesweeper.field.cells

import android.graphics.Canvas

sealed class Cell {
    abstract fun renderTo(canvas: Canvas, xStart: Int, yStart: Int, xEnd: Int, yEnd: Int)
    abstract fun typeString(): String
}