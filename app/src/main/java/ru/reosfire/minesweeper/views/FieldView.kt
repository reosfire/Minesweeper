package ru.reosfire.minesweeper.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.reosfire.minesweeper.game.Game
import kotlin.math.min

class FieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val black = Paint().apply {
        color = Color.rgb(50, 50, 50)
    }
    private val white = Paint().apply {
        color = Color.rgb(150, 150, 150)
    }

    var game: Game? = null
        set(value) {
            field?.getField()?.unsubscribeFromUpdates()
            field = value
            field?.getField()?.subscribeToUpdates {
                invalidate()
            }
            invalidate()
        }


    private var cellSize = 0f
    private var gridArea = RectF(0f, 0f, 0f, 0f)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val field = game?.getField() ?: return

        val availW = w - paddingLeft - paddingRight
        val availH = h - paddingTop - paddingBottom

        val maxCellH = availH / field.height
        val maxCellW = availW / field.width

        cellSize = min(maxCellH, maxCellW).toFloat()

        val gridHeight = cellSize * field.height
        val gridWidth = cellSize * field.width

        gridArea = RectF(0f, 0f, gridWidth, gridHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        val field = game?.getField() ?: return

        for (i in 0 until field.height) {
            for (j in 0 until field.width) {
                val paint = if ((i + j) % 2 == 0) black else white

                canvas.drawRect(gridArea.left + j * cellSize, gridArea.top + i * cellSize,
                    gridArea.left + (j + 1) * cellSize, gridArea.top + (i + 1) * cellSize, paint)

                field.get(i, j).renderTo(canvas, gridArea.left + j * cellSize, gridArea.top + i * cellSize,
                    gridArea.left + (j + 1) * cellSize, gridArea.top + (i + 1) * cellSize)
            }
        }

        super.onDraw(canvas)
    }
}