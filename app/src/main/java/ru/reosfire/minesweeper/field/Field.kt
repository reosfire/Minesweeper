package ru.reosfire.minesweeper.field

import ru.reosfire.minesweeper.field.cells.Cell
import ru.reosfire.minesweeper.field.cells.EmptyCell

typealias FieldUpdateListener = () -> Unit

class Field(val width: Int, val height: Int) {
    private val data = Array(height) { Array<Cell>(width) { EmptyCell() } }
    private var updateHandler: FieldUpdateListener? = null

    fun get(x: Int, y: Int): Cell {
        return data[x][y]
    }
    fun set(x: Int, y: Int, cell: Cell) {
        data[x][y] = cell
        updateHandler?.invoke()
    }

    fun setAll(cells: List<Triple<Int, Int, Cell>>) {
        for (cell in cells) {
            data[cell.first][cell.second] = cell.third
        }
        updateHandler?.invoke()
    }

    fun subscribeToUpdates(handler: FieldUpdateListener) {
        updateHandler = handler
    }
    fun unsubscribeFromUpdates() {
        updateHandler = null
    }
}