package ru.reosfire.minesweeper.field.cells

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log

class NumberCell(private val number: Int): Cell() {
    companion object {
        val paints = arrayOf(
            Paint().apply {
                color = Color.rgb(150, 0, 0)
                textSize = 100f
                textAlign = Paint.Align.CENTER
            },
            Paint().apply {
                color = Color.rgb(0, 150, 0)
                textSize = 100f
                textAlign = Paint.Align.CENTER
            },
            Paint().apply {
                color = Color.rgb(0, 0, 150)
                textSize = 100f
                textAlign = Paint.Align.CENTER
            })

        private val bounds = Rect()
    }

    override fun renderTo(canvas: Canvas, xStart: Float, yStart: Float, xEnd: Float, yEnd: Float) {
        val paint = paints[number % paints.size]
        paint.getTextBounds(number.toString(), 0, 1, bounds)
        val numberH = bounds.bottom - bounds.top
        val emptyH = yEnd - numberH - yStart
        canvas.drawText(number.toString(), (xStart + xEnd) / 2, yEnd - emptyH / 2, paint)
    }
}