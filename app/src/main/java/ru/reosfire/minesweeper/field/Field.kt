package ru.reosfire.minesweeper.field

import ru.reosfire.minesweeper.field.cells.Cell
import ru.reosfire.minesweeper.field.cells.EmptyCell

class Field(val width: Int, val height: Int) {
    val data = Array(height) { Array<Cell>(width) { EmptyCell() } }
}