package ru.reosfire.minesweeper.field.cells

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class NumberCell(private val number: Int): Cell() {
    companion object {
        val paints = arrayOf(
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(211, 249, 181)
                textSize = 100f
                textAlign = Paint.Align.CENTER
            },
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(239, 71, 111)
                textSize = 100f
                textAlign = Paint.Align.CENTER
            },
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(250, 169, 22)
                textSize = 100f
                textAlign = Paint.Align.CENTER
            })

        val zeroPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(50, 210, 255, 252)
        }

        private val bounds = Rect()
    }

    override fun renderTo(canvas: Canvas, xStart: Float, yStart: Float, xEnd: Float, yEnd: Float) {
        canvas.drawRect(xStart, yStart, xEnd, yEnd, zeroPaint)

        if (number == 0) return

        val paint = paints[number % paints.size]
        paint.getTextBounds(number.toString(), 0, 1, bounds)
        val numberH = bounds.bottom - bounds.top
        val emptyH = yEnd - numberH - yStart
        canvas.drawText(number.toString(), (xStart + xEnd) / 2, yEnd - emptyH / 2, paint)
    }
}