package ru.reosfire.minesweeper.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.reosfire.minesweeper.R
import ru.reosfire.minesweeper.field.cells.EmptyCell
import ru.reosfire.minesweeper.field.cells.FlagCell
import ru.reosfire.minesweeper.field.cells.NumberCell
import ru.reosfire.minesweeper.game.Game
import ru.reosfire.minesweeper.game.GameSettings
import java.util.*
import kotlin.math.min

class FieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.FieldViewStyle,
    defStyleRes: Int = R.style.DefaultFieldStyle
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private val backgroundColorFirst = Paint()
    private val backgroundColorSecond = Paint()

    var game: Game? = null
        set(value) {
            field?.getField()?.unsubscribeFromUpdates()
            field = value
            field?.getField()?.subscribeToUpdates {
                invalidate()
            }
            invalidate()
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

        if (isInEditMode) {
            game = Game(GameSettings(10, 5, 20))

            val rnd = Random()
            with(game!!) {
                for (i in 0 until this.getField().height) {
                    for (j in 0 until this.getField().width) {
                        when (rnd.nextInt(5)) {
                            0 -> getField().set(i, j, FlagCell())
                            1 -> getField().set(i, j, NumberCell(rnd.nextInt(9) + 1))
                            else -> getField().set(i, j, EmptyCell())
                        }
                    }
                }
            }
        }
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
        val field = game?.getField() ?: return

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
}