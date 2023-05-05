package ru.reosfire.minesweeper.game

import ru.reosfire.minesweeper.field.Field
import java.util.BitSet
import java.util.Random

class Game(private val settings: GameSettings) {
    private val random = Random()
    private val field = Field(settings.width, settings.height)
    private val mines = Array(settings.height) { BitSet(settings.width) }
    private var started = false

    fun open(x: Int, y: Int): OpenResult {
        if (!started) fillMinesWithout(x, y)
        if (mines[x][y]) return OpenResult.Loose

        //TODO open logic
        return OpenResult.Win
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