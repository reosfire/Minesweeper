package ru.reosfire.minesweeper.field.cells

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class FlagCell: Cell() {
    companion object {
        private val COLOR = Color.rgb(200, 40, 40)

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = COLOR
            style = Paint.Style.FILL
        }

        private val colors = IntArray(6) { COLOR }

        private val vertices = FloatArray(6)
    }

    override fun renderTo(canvas: Canvas, xStart: Int, yStart: Int, xEnd: Int, yEnd: Int) {
        val w = xEnd - xStart
        val lineW = w * 0.1f
        val lineH = w * 0.8f
        val centerW = (xEnd - xStart) / 2 + xStart
        val centerH = (yEnd - yStart) / 2 + yStart
        canvas.drawRect(centerW - lineW / 2, centerH - lineH / 2, centerW + lineW / 2, centerH + lineH / 2, paint)

        vertices[0] = centerW - lineW / 2 + 1
        vertices[1] = centerH - lineH / 2

        vertices[2] = centerW - lineW / 2 + lineH / 2
        vertices[3] = centerH - lineH / 4

        vertices[4] = centerW - lineW / 2 + 1
        vertices[5] = centerH.toFloat()

        canvas.drawVertices(Canvas.VertexMode.TRIANGLES, vertices.size,
            vertices, 0,
            null,
            0,
            colors,
            0,
            null,
            0,
            0,
            paint)
    }

    override fun typeString(): String {
        return "flag"
    }
}