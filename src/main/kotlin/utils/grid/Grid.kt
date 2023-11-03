package utils.grid

import utils.point.Point
import kotlin.math.absoluteValue

@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Grid<T>(var rows: Int = 0, var columns: Int = 0) : Collection<T> {
    val data = mutableMapOf<Point, T>()
    val minX get() = data.keys.minOf { it.x }
    val minY get() = data.keys.minOf { it.y }
    val maxX get() = data.keys.maxOf { it.x }
    val maxY get() = data.keys.maxOf { it.y }
    val min get() = Point(minX, minY)
    val max get() = Point(maxX, maxY)

    operator fun set(points: Set<Point>, value: T) = points.forEach { this[it] = value }
    fun add(points: Set<Point>, value: T) = points.forEach { this[it] = this[it] ?: value }
    operator fun set(row: Int, column: Int, value: T) = set(Point(row, column), value)
    operator fun set(point: Point, value: T) {
        data[point] = value
        rows = maxOf(rows, point.x + 1)
        columns = maxOf(columns, point.y + 1)
    }

    operator fun get(point: Point): T? = data[point]
    operator fun get(row: Int, col: Int): T? = data[Point(row, col)]
    operator fun get(value: T): Point = data.entries.find { it.value == value }?.key ?: throw Exception("Value not found")
    fun getOrDefault(point: Point, defaultValue: T): T = data.getOrDefault(point, defaultValue)
    fun getOrDefault(value: T, defaultPoint: Point): Point = data.entries.find { it.value == value }?.key ?: defaultPoint
    fun getPointsWithValue(value: T): Set<Point> = data.filterValues { it == value }.keys

    fun replace(oldValue: T, newValue: T): Grid<T> {
        val position = getPointsWithValue(oldValue)
        position.forEach { data[it] = newValue }
        return this
    }

    fun getRow(row: Int): List<T?> {
        val rowElements = mutableListOf<T?>()
        for (col in minY..maxY) {
            rowElements.add(get(Point(row, col)))
        }
        return rowElements
    }

    fun getColumn(col: Int): List<T?> {
        val colElements = mutableListOf<T?>()
        for (row in minX..maxX) {
            colElements.add(get(Point(row, col)))
        }
        return colElements
    }

    fun getRows(): List<List<T?>> {
        val rowsElements = mutableListOf<List<T?>>()
        for (row in minX..maxX) {
            rowsElements.add(getRow(row))
        }
        return rowsElements
    }

    fun getColumns(): List<List<T?>> {
        val colsElements = mutableListOf<List<T?>>()
        for (col in minY..maxY) {
            colsElements.add(getColumn(col))
        }
        return colsElements
    }

    fun getDiagonals(): List<List<T?>> {
        val diagonal1 = (0..<rows).map { get(Point(it, it)) }
        val diagonal2 = (0..<rows).map { get(Point(it, rows - it - 1)) }
        return listOf(diagonal1, diagonal2)
    }

    fun filter(predicate: (Map.Entry<Point, T>) -> Boolean): Map<Point, T> {
        return data.filter(predicate)
    }

    fun all(predicate: (Map.Entry<Point, T>) -> Boolean): Boolean {
        return data.all(predicate)
    }

    fun any(predicate: (Map.Entry<Point, T>) -> Boolean): Boolean {
        return data.any(predicate)
    }

    fun none(predicate: (Map.Entry<Point, T>) -> Boolean): Boolean {
        return data.none(predicate)
    }

    fun <R> map(transform: (Map.Entry<Point, T>) -> R): List<R> {
        return data.map(transform)
    }

    fun forEach(action: (Map.Entry<Point, T>) -> Unit) {
        data.forEach(action)
    }

    fun fill(points: Set<Point>, value: Char): Grid<Char?> {
        val rows = points.maxOf { it.x } + 1
        val columns = points.maxOf { it.y } + 1

        val grid = Grid<Char?>(rows, columns)

        points.forEach { point ->
            grid[point] = value
        }

        return grid
    }

    fun getCardinalNeighborPositions(row: Int, column: Int): Set<Point> {
        val neighbors = mutableSetOf<Point>()
        val relativePositions = listOf(Point(-1, 0), Point(0, -1), Point(0, 1), Point(1, 0))
        for (position in relativePositions) {
            val potentialNeighbor = Point(row + position.x, column + position.y)
            if (data.containsKey(potentialNeighbor)) {
                neighbors.add(potentialNeighbor)
            }
        }
        return neighbors
    }

    fun getNeighborPositions(row: Int, column: Int): Set<Point> {
        val neighbors = mutableSetOf<Point>()
        val relativePositions = listOf(
            Point(-1, -1), Point(-1, 0), Point(-1, 1), Point(0, -1), Point(0, 1), Point(1, -1), Point(1, 0), Point(1, 1)
        )
        for (position in relativePositions) {
            val potentialNeighbor = Point(row + position.x, column + position.y)
            if (data.containsKey(potentialNeighbor)) {
                neighbors.add(potentialNeighbor)
            }
        }
        return neighbors
    }

    fun getNeighbors(row: Int, column: Int): Set<T?> =
        getNeighborPositions(row, column).map { get(Point(it.x, it.y)) }.toSet()

    fun getCardinalNeighbors(row: Int, column: Int): Set<T?> =
        getCardinalNeighborPositions(row, column).map { get(Point(it.x, it.y)) }.toSet()

    fun movePointsBy(points: Set<Point>, xOff: Long, yOff:Long): Set<Point> {
        val newGrid = mutableMapOf<Point, T>()
        points.forEach { point ->
            val value = this.data[point]
            if (value != null) {
                this.data.remove(point)
                newGrid[Point(point.x + xOff, point.y + yOff)] = value
            }
        }
        this.data.putAll(newGrid)
        return newGrid.keys
    }

    fun getHighestOfValue(value: T): Point? = data.filterValues { it == value }.keys.maxByOrNull { it.y }

    fun getLowestOfValue(value: T): Point? = data.filterValues { it == value }.keys.minByOrNull { it.y }

    fun getRightmostOfValue(value: T): Point? = data.filterValues { it == value }.keys.maxByOrNull { it.x }

    fun getLeftmostOfValue(value: T): Point? = data.filterValues { it == value }.keys.minByOrNull { it.x }

    fun getHighest(num: Int): Grid<T> {
        val newGrid = Grid<T>(num, this.columns)
        for (i in 0 ..< num) {
            for (j in 0 ..< this.columns) {
                val value = this[i, j]
                if (value != null) {
                    newGrid[i, j] = value
                }
            }
        }
        return newGrid
    }

    fun getLowest(num: Int): Grid<T> {
        val newGrid = Grid<T>(num, this.columns)
        for (i in 0 ..< num)
            for (j in 0 ..< this.columns)
                newGrid[i,j] = this[this.rows - num + i,j]!!
        return newGrid
    }

    fun getLeftmost(num: Int): Grid<T> {
        val newGrid = Grid<T>(this.rows, num)
        for (i in 0 ..< this.rows)
            for (j in 0 ..< num)
                newGrid[i, j] = this[i, j]!!
        return newGrid
    }

    fun getRightmost(num: Int): Grid<T> {
        val newGrid = Grid<T>(this.rows, num)
        for (i in 0 ..< this.rows)
            for (j in 0 ..< num)
                newGrid[i, j] = this[i, this.columns - num + j]!!
        return newGrid
    }
    
    fun fillWith(value: T): Grid<T> {
        for (row in 0..<rows) {
            for (column in 0..<columns) {
                if (get(row, column) != null) continue
                set(row, column, value)
            }
        }
        return this
    }

    fun floodFill(startPoint: Point, newValue: T): Grid<T> {
        val oldValue = get(startPoint) ?: throw NullPointerException("Cannot flood fill null value, try using fillWith() first")

        val stack = mutableListOf(startPoint)

        while (stack.isNotEmpty()) {
            val currentPoint = stack.removeAt(stack.size - 1)

            if (currentPoint.x in 0 ..< rows && currentPoint.y in 0 ..< columns &&
                get(currentPoint) == oldValue) {

                this[currentPoint] = newValue

                stack.add(Point(currentPoint.x + 1, currentPoint.y))
                stack.add(Point(currentPoint.x - 1, currentPoint.y))
                stack.add(Point(currentPoint.x, currentPoint.y + 1))
                stack.add(Point(currentPoint.x, currentPoint.y - 1))
            }
        }
        return this
    }

    fun addPoints(xRange: IntRange, yRange: IntRange, value: T): Grid<T> {
        for (x in xRange) {
            for (y in yRange) {
                set(x, y, value)
            }
        }
        return this
    }

    fun invert(): Grid<T> {
        val newGrid = Grid<T>(columns, rows)

        for ((point, value) in this.data) {
            val newPoint = Point(point.y, point.x)
            newGrid[newPoint] = value
        }

        return newGrid
    }

    fun isInside(x: Int, y: Int): Boolean {
        return x in 0..<rows && y in 0..<columns
    }

    fun isOutside(x: Int, y: Int): Boolean {
        return !(x in 0..<rows && y in 0..<columns)
    }

    fun canMove(points: Set<Point>, dx: Int, dy: Int): Boolean {
        return points.all { point ->
            val newX = point.x + dx
            val newY = point.y + dy
            this[newX, newY] == null || Point(newX, newY) in points
        }
    }

    override fun toString(): String {
        val rowsInGrid = (minY..maxY)
        val columnsInGrid = (minX..maxX)

        val lineSeparator = System.lineSeparator()
        return rowsInGrid.joinToString(separator = lineSeparator) { row ->
            columnsInGrid.joinToString(separator = " ") { column ->
                get(Point(column, row))?.toString() ?: " "
            }
        }
    }

    fun toString(default: String = " ", invertVertically: Boolean = false, startFromOrigin: Boolean = false): String {
        val startRow = if (startFromOrigin) 0 else minY
        val startCol = if (startFromOrigin) 0 else minX
        val endRow = maxY
        val endCol = maxX

        val maxRowDigits = (endRow.toString().length).coerceAtLeast((startRow.absoluteValue).toString().length) + 1
        val maxColDigits = endCol.toString().length

        return buildString {
            for (i in 0 ..< maxColDigits) {
                append(" ".repeat(maxRowDigits))
                for (col in startCol..endCol) {
                    val ch = col.toString().padStart(maxColDigits)[i]
                    append(" ").append(ch)
                }
                appendLine()
            }
            for (row in if (invertVertically) endRow downTo startRow else startRow..endRow) {
                val rowStr = row.toString().padStart(maxRowDigits, ' ')
                append(rowStr).append(" ")
                for (col in startCol..endCol) {
                    val cellValue = get(Point(col, row))?.toString() ?: default
                    append(cellValue).append(" ")
                }
                appendLine()
            }
        }
    }

    override fun contains(element: T): Boolean = data.containsValue(element)

    override val size: Int get() = data.size

    override fun containsAll(elements: Collection<T>): Boolean = elements.all { this.contains(it) }

    override fun isEmpty(): Boolean = data.isEmpty()

    override fun iterator(): Iterator<T> = data.values.iterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid<*>

        if (rows != other.rows) return false
        if (columns != other.columns) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        result = 31 * result + data.hashCode()
        return result
    }

    fun deepCopy(): Grid<T> {
        val newGrid = Grid<T>(this.rows, this.columns)
        this.data.forEach { (point, value) ->
            newGrid[point.x, point.y] = value
        }
        return newGrid
    }

    companion object {
        fun <T> of(rows: List<List<T>>): Grid<T?> {
            val grid = Grid<T?>()
            for (i in rows.indices) {
                for (j in rows[i].indices) {
                    grid[Point(j, i)] = rows[i][j]
                }
            }
            return grid
        }

        @JvmName("ofSingleList")
        fun <T : Any> of(rows: List<T>): Grid<T?> {
            val grid = Grid<T?>()
            rows.forEachIndexed { i, value ->
                grid[Point(0, i)] = value
            }
            return grid
        }

        @JvmName("ofStringList")
        fun of(rows: List<String>): Grid<Char?> {
            val grid = Grid<Char?>()
            for (i in rows.indices) {
                for (j in rows[i].indices) {
                    grid[Point(j, i)] = rows[i][j]
                }
            }
            return grid
        }
    }


}
