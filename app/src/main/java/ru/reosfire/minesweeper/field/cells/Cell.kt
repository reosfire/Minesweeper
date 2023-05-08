package ru.reosfire.minesweeper.field.cells

import android.graphics.Canvas

abstract class Cell {
    abstract fun renderTo(canvas: Canvas, xStart: Int, yStart: Int, xEnd: Int, yEnd: Int)
}