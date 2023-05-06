package ru.reosfire.minesweeper.field.cells

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class EmptyCell: Cell() {
    companion object {
        val paint = Paint().apply {
            color = Color.rgb(200, 0, 0)
        }
    }
    override fun renderTo(canvas: Canvas, xStart: Float, yStart: Float, xEnd: Float, yEnd: Float) {

    }
}