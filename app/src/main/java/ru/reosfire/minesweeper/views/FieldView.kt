package ru.reosfire.minesweeper.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ru.reosfire.minesweeper.R
import ru.reosfire.minesweeper.field.Field
import kotlin.math.min

typealias CellClickedListener = (Int, Int) -> Unit

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

    private var cellClickedListener: CellClickedListener? = null

    private val backgroundColorFirst = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundColorSecond = Paint(Paint.ANTI_ALIAS_FLAG)
    private val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
    }


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


    private var cellSize = 0
    private var gridArea = Rect(0, 0, 0, 0)

    private var selectedX = -1
    private var selectedY = -1

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val field = gameField ?: return

        val availW = w - paddingLeft - paddingRight
        val availH = h - paddingTop - paddingBottom

        val maxCellH = availH / field.height
        val maxCellW = availW / field.width

        cellSize = min(maxCellH, maxCellW)

        val gridHeight = cellSize * field.height
        val gridWidth = cellSize * field.width

        val emptyH = availH - gridHeight
        val emptyW = availW - gridWidth

        val left = emptyW / 2
        val top = emptyH / 2

        gridArea = Rect(left, top, left + gridWidth, top + gridHeight)
    }

    private val rect = Rect()

    private fun getRectForCell(i: Int, j: Int):Rect {
        return rect.apply {
            left = gridArea.left + j * cellSize
            top = gridArea.top + i * cellSize
            right = left + cellSize
            bottom = top + cellSize
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        val field = gameField ?: return

        for (i in 0 until field.height) {
            for (j in 0 until field.width) {
                val paint = if ((i + j) % 2 == 0) backgroundColorFirst else backgroundColorSecond

                val rect = getRectForCell(i, j)

                canvas.drawRect(rect, paint)

                field.get(i, j).renderTo(canvas, rect.left, rect.top, rect.right, rect.bottom)
            }
        }

        if (!isInvalidSelected()) {
            val rect = getRectForCell(selectedY, selectedX)
            canvas.drawRect(rect, highlightPaint)
        }

        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        if (gameField == null) return false

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_MOVE -> {
                updateSelectedCell(event)
                true
            }
            MotionEvent.ACTION_UP -> {
                updateSelectedCell(event)
                performClick()
                true
            }

            else -> false
        }
    }

    private fun updateSelectedCell(event: MotionEvent) {
        val x = getGameX(event.x)
        val y = getGameY(event.y)
        if (x != selectedX || y != selectedY) invalidate()
        selectedX = x
        selectedY = y
    }

    private fun isInvalidSelected():Boolean {
        val field = gameField ?: return false
        return selectedX < 0 || selectedY < 0 || selectedX >= field.width || selectedY >= field.height
    }

    private fun getGameX(componentX: Float): Int {
        val inGridX = componentX - gridArea.left
        if (inGridX < 0) return -1
        return (inGridX / cellSize).toInt()
    }
    private fun getGameY(componentY: Float): Int {
        val inGridY = componentY - gridArea.top
        if (inGridY < 0) return -1
        return (inGridY / cellSize).toInt()
    }

    fun setCellClickedListener(listener: CellClickedListener) {
        cellClickedListener = listener
    }

    override fun performClick(): Boolean {
        val result = super.performClick()

        if (isInvalidSelected()) return result

        cellClickedListener?.invoke(selectedX, selectedY)

        return true
    }
}