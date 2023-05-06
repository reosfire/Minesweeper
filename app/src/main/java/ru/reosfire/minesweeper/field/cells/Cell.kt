package ru.reosfire.minesweeper.field.cells

import android.graphics.Canvas

abstract class Cell {
    abstract fun renderTo(canvas: Canvas, xStart: Float, yStart: Float, xEnd: Float, yEnd: Float)
}