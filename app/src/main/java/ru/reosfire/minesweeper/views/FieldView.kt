package ru.reosfire.minesweeper.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ru.reosfire.minesweeper.R
import ru.reosfire.minesweeper.field.Field
import ru.reosfire.minesweeper.field.cells.FlagCell
import kotlin.math.min

typealias OnCellClicked = (Int, Int) -> Void

class FieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.FieldViewStyle,
    defStyleRes: Int = R.style.DefaultFieldStyle
) : View(context, attrs, defStyleAttr, defStyleRes) {
    var gameField: Field? = null
        set(value) {
            field?.unsubscribeFromUpdates()
            field = value
            field?.subscribeToUpdates {
                invalidate()
            }
            invalidate()
        }

    private val backgroundColorFirst = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundColorSecond = Paint(Paint.ANTI_ALIAS_FLAG)


    init {
        context.obtainStyledAttributes(attrs, R.styleable.FieldView, defStyleAttr, defStyleRes).apply {
            backgroundColorFirst.apply {
                color = getColor(R.styleable.FieldView_background_color_first, Color.BLACK)
            }
            backgroundColorSecond.apply {
                color = getColor(R.styleable.FieldView_background_color_second, Color.BLACK)
            }
        }.recycle()
    }


    private var cellSize = 0f
    private var gridArea = RectF(0f, 0f, 0f, 0f)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val field = gameField ?: return

        val availW = w - paddingLeft - paddingRight
        val availH = h - paddingTop - paddingBottom

        val maxCellH = availH / field.height
        val maxCellW = availW / field.width

        cellSize = min(maxCellH, maxCellW).toFloat()

        val gridHeight = cellSize * field.height
        val gridWidth = cellSize * field.width

        val emptyH = availH - gridHeight
        val emptyW = availW - gridWidth

        val left = emptyW / 2
        val top = emptyH / 2

        gridArea = RectF(left, top, left + gridWidth, top + gridHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        val field = gameField ?: return

        for (i in 0 until field.height) {
            for (j in 0 until field.width) {
                val paint = if ((i + j) % 2 == 0) backgroundColorFirst else backgroundColorSecond

                canvas.drawRect(gridArea.left + j * cellSize, gridArea.top + i * cellSize,
                    gridArea.left + (j + 1) * cellSize, gridArea.top + (i + 1) * cellSize, paint)

                field.get(i, j).renderTo(canvas, gridArea.left + j * cellSize, gridArea.top + i * cellSize,
                    gridArea.left + (j + 1) * cellSize, gridArea.top + (i + 1) * cellSize)
            }
        }

        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        val field = gameField ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                val inGridX = event.x - gridArea.left
                val inGridY = event.y - gridArea.top

                val x = (inGridX / cellSize).toInt()
                val y = (inGridY / cellSize).toInt()

                if (x < 0 || y < 0 || x >= field.width || y >= field.height) return false

                field.set(y, x, FlagCell())

                return true
            }
            else -> return false
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}