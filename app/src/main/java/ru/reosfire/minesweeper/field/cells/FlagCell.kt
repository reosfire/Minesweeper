package ru.reosfire.minesweeper.field.cells

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class FlagCell: Cell() {
    companion object {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(200, 40, 40)
            style = Paint.Style.FILL
        }
    }

    override fun renderTo(canvas: Canvas, xStart: Float, yStart: Float, xEnd: Float, yEnd: Float) {
        val w = xEnd - xStart
        val lineW = w * 0.1f
        val lineH = w * 0.8f
        val centerW = (xEnd - xStart) / 2 + xStart
        val centerH = (yEnd - yStart) / 2 + yStart
        canvas.drawRect(centerW - lineW / 2, centerH - lineH / 2, centerW + lineW / 2, centerH + lineH / 2, paint)

        val vertices = floatArrayOf(
            centerW - lineW / 2 + 1, centerH - lineH / 2,
            centerW - lineW / 2 + 40, centerH - lineH / 4,
            centerW - lineW / 2 + 1, centerH)

        canvas.drawVertices(Canvas.VertexMode.TRIANGLES, vertices.size,
            vertices, 0,
            null,
            0,
            intArrayOf(Color.rgb(200, 10, 10),
                Color.rgb(200, 10, 10),
                Color.rgb(200, 10, 10),
                Color.rgb(200, 10, 10),
                Color.rgb(200, 10, 10),
                Color.rgb(200, 10, 10)),
            0,
            null,
            0,
            0,
            paint
            )
    }
}