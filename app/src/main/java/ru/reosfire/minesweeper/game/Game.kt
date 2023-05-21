package ru.reosfire.minesweeper.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import ru.reosfire.minesweeper.field.Field
import ru.reosfire.minesweeper.field.cells.Cell
import ru.reosfire.minesweeper.field.cells.EmptyCell
import ru.reosfire.minesweeper.field.cells.FlagCell
import ru.reosfire.minesweeper.field.cells.NumberCell
import java.util.*
import kotlin.math.max
import kotlin.math.min

class Game(val settings: GameSettings) {
    private val random = Random()
    private val mines = Array(settings.height) { BitSet(settings.width) }
    private val numbers = Array(settings.height) { IntArray(settings.width) }
    val field = Field(settings.width, settings.height)
    var timer = Timer()
        private set
    var started = false
        private set
    var flags = 0
        private set
    var creationTime = System.currentTimeMillis()
        private set
    var completed = false
        private set

    var stateId: Int = 0
        private set

    constructor(state: GameState): this(state.getSettings()) {
        stateId = state.id

        val minesJson = Json.decodeFromString<JsonArray>(state.minesJson)
        for (mineElement in minesJson) {
            val i = mineElement.jsonObject["i"]?.jsonPrimitive?.int ?: 0
            val j = mineElement.jsonObject["j"]?.jsonPrimitive?.int ?: 0
            mines[i][j] = true
        }
        calculateNumbers()

        val fieldJson = Json.decodeFromString<JsonArray>(state.fieldJson)
        for (fieldElement in fieldJson) {
            val i = fieldElement.jsonObject["i"]!!.jsonPrimitive.int
            val j = fieldElement.jsonObject["j"]!!.jsonPrimitive.int
            val cell = when (fieldElement.jsonObject["type"]!!.jsonPrimitive.content) {
                "empty" -> EmptyCell()
                "number" -> NumberCell(fieldElement.jsonObject["number"]!!.jsonPrimitive.int)
                "flag" -> {
                    flags++
                    FlagCell()
                }
                else -> EmptyCell()
            }

            field.set(i, j, cell)
        }

        creationTime = state.creationTime
        completed = state.completed
        timer = Timer(state.time)

        if (!completed) timer.run(MainScope())
        started = true
    }

    fun getState(): GameState {
        val minesJson = buildJsonArray {
            for (i in 0 until settings.height) {
                for (j in 0 until settings.width) {
                    if (!mines[i][j]) continue

                    this@buildJsonArray.add(buildJsonObject {
                        put("i", i as Number)
                        put("j", j as Number)
                    })
                }
            }
        }

        val fieldJson = buildJsonArray {
            for (i in 0 until settings.height) {
                for (j in 0 until settings.width) {
                    val current = field.get(i, j)
                    this@buildJsonArray.add(buildJsonObject {
                        put("type", current.typeString())
                        if (current is NumberCell) put("number", current.number)
                        put("i", i as Number)
                        put("j", j as Number)
                    })
                }
            }
        }

        return GameState(settings.height, settings.width,
            minesJson.toString(), fieldJson.toString(),
            creationTime,
            timer.seconds.value!!,
            completed, stateId)
    }

    private fun openActually(x: Int, y: Int): OpenResult {
        if (field.get(x, y) is FlagCell) return OpenResult.NothingChanged

        if (!started) {
            fillMinesWithout(x, y)
            calculateNumbers()
            timer.run(MainScope())
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

    fun open(x: Int, y: Int): OpenResult {
        val result = openActually(x, y)

        if (result == OpenResult.Win || result == OpenResult.Loose) {
            completed = true
            timer.stop()
        }

        return result
    }

    fun toggleFlag(x: Int, y:Int) {
        if (field.get(x, y) is FlagCell) {
            field.set(x, y, EmptyCell())
            flags--
        }
        else if (field.get(x, y) is EmptyCell) {
            field.set(x, y, FlagCell())
            flags++
        }
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

    class Timer(initSeconds: Int = 0) {
        private var timerJob: Job? = null
        private val currentSeconds = MutableLiveData(initSeconds)
        val seconds: LiveData<Int> = currentSeconds

        fun stop() {
            timerJob?.cancel()
        }
        fun run(scope: CoroutineScope) {
            timerJob = scope.launch {
                while (isActive) {
                    currentSeconds.value = currentSeconds.value!! + 1
                    delay(1000)
                }
            }
        }
    }
}