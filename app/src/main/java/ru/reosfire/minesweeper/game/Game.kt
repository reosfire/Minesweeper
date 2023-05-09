package ru.reosfire.minesweeper.game

import ru.reosfire.minesweeper.field.Field
import ru.reosfire.minesweeper.field.cells.Cell
import ru.reosfire.minesweeper.field.cells.NumberCell
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Game(private val settings: GameSettings) {
    companion object {

    }

    private val random = Random()
    private val field = Field(settings.width, settings.height)
    private val mines = Array(settings.height) { BitSet(settings.width) }
    private val numbers = Array(settings.height) { IntArray(settings.width) }
    private var started = false

    fun getField(): Field {
        return field
    }

    fun getState(): GameState {
        val cellCount = settings.cellsCount()
        val minesString = CharArray(cellCount)
        var k = 0
        for (i in 0 until settings.height) {
            for (j in 0 until settings.width) {
                minesString[k] = if (mines[i][j]) '1' else '0'
                k++
            }
        }

        val fieldString = StringBuilder()


        return GameState(settings.height, settings.width, minesString.concatToString(), fieldString.toString())
    }

    fun open(x: Int, y: Int): OpenResult {
        if (!started) {
            fillMinesWithout(x, y)
            calculateNumbers()
            started = true
        }
        if (mines[x][y]) return OpenResult.Loose

        field.setAll(searchOpened(x, y))

        var unsetCount = 0

        for (i in 0 until settings.height) {
            for (j in 0 until settings.width) {
                if (field.get(i, j) !is NumberCell) unsetCount++
            }
        }

        return if (unsetCount == settings.minesCount) OpenResult.Win else OpenResult.Updated
    }

    private fun searchOpened(startX: Int, startY: Int): List<Triple<Int, Int, Cell>> {
        val result = mutableListOf<Triple<Int, Int, Cell>>()

        val visited = Array(settings.height) { BitSet(settings.width) }
        fun dfs(x: Int, y: Int) {
            if (visited[x][y]) return
            visited[x][y] = true

            result.add(Triple(x, y, NumberCell(numbers[x][y])))

            if (numbers[x][y] != 0) return

            for (i in max(0, x - 1)..min(settings.height - 1, x + 1)) {
                for (j in max(0, y - 1)..min(settings.width - 1, y + 1)) {
                    dfs(i, j)
                }
            }
        }

        dfs(startX, startY)

        return result
    }


    private fun calculateNumbers() {
        for (i in 0 until settings.height) {
            for (j in 0 until settings.width) {
                numbers[i][j] = calculateNumber(i, j)
            }
        }
    }

    private fun calculateNumber(x: Int, y: Int): Int {
        if (mines[x][y]) return 0
        var result = 0

        for (i in max(0, x - 1)..min(settings.height - 1, x + 1)) {
            for (j in max(0, y - 1)..min(settings.width - 1, y + 1)) {
                if (mines[i][j]) result++
            }
        }

        return result
    }

    private fun randomPoint(): Pair<Int, Int> {
        return Pair(random.nextInt(settings.height), random.nextInt(settings.width))
    }
    private fun fillMinesWithout(x: Int, y: Int) {
        val generated = mutableSetOf<Pair<Int, Int>>()

        while (generated.size < settings.minesCount) {
            val current = randomPoint()
            if (current.first == x && current.second == y) continue

            generated.add(current)
        }

        for (point in generated) {
            mines[point.first][point.second] = true
        }
    }
}