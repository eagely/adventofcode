package utils.grid

import utils.point.Point
import kotlin.math.absoluteValue

data class Grid<T>(var rows: Int = 0, var columns: Int = 0) : Collection<T> {
    val grid: MutableMap<Point, T> = mutableMapOf()
    val minX: Int get() = grid.keys.minOf { it.x }
    val minY: Int get() = grid.keys.minOf { it.y }
    val maxX: Int get() = grid.keys.maxOf { it.x }
    val maxY: Int get() = grid.keys.maxOf { it.y }
    val min: Point get() = Point(minX, minY)
    val max: Point get() = Point(maxX, maxY)
    operator fun set(points: Set<Point>, value: T) {
        for (point in points) this[point] = value
    }
    fun add(points: Set<Point>, value: T) {
        for (point in points) {
            if (this[point] == null) this[point] = value
        }
    }
    operator fun set(row: Int, column: Int, value: T) {
        grid[Point(row, column)] = value
        rows = maxOf(rows, row + 1)
        columns = maxOf(columns, column + 1)
    }
    operator fun set(point: Point, value: T) {
        grid[point] = value
        rows = maxOf(rows, point.x + 1)
        columns = maxOf(columns, point.y + 1)
    }
    operator fun get(point: Point): T? = grid[point]
    operator fun get(x: Int, y: Int): T? = grid[Point(x, y)]
    operator fun get(value: T): Point = grid.entries.find { it.value == value }?.key ?: throw Exception("Value not found")
    fun getOrDefault(point: Point, defaultValue: T): T = grid.getOrDefault(point, defaultValue)
    fun getOrDefault(value: T, defaultPoint: Point): Point = grid.entries.find { it.value == value }?.key ?: defaultPoint
    fun getPointsWithValue(value: T): Set<Point> = grid.filterValues { it == value }.keys.toSet()
    fun replace(oldValue: T, newValue: T) {
        val position = get(oldValue)
        position.let { grid[it] = newValue }
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

    fun getCardinalNeighborPositions(row: Int, column: Int): Set<Point> {
        val neighbors = mutableSetOf<Point>()

        val relativePositions = listOf(
            Point(-1, 0), Point(0, -1), Point(0, 1), Point(1, 0)
        )

        for (position in relativePositions) {
            val potentialNeighbor = Point(row + position.x, column + position.y)
            if (grid.containsKey(potentialNeighbor)) {
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
            if (grid.containsKey(potentialNeighbor)) {
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
            val value = this.grid[point]
            if (value != null) {
                this.grid.remove(point)
                newGrid[Point(point.x + xOff, point.y + yOff)] = value
            }
        }
        this.grid.putAll(newGrid)
        return newGrid.keys
    }

    fun getHighestOfValue(value: T): Point? = grid.filterValues { it == value }.keys.maxByOrNull { it.y }

    fun getLowestOfValue(value: T): Point? = grid.filterValues { it == value }.keys.minByOrNull { it.y }

    fun getRightmostOfValue(value: T): Point? = grid.filterValues { it == value }.keys.maxByOrNull { it.x }

    fun getLeftmostOfValue(value: T): Point? = grid.filterValues { it == value }.keys.minByOrNull { it.x }

    fun getTopRows(num: Int): Grid<T> {
        val newGrid = Grid<T>(num, this.columns)
        for (i in 0 until num) {
            for (j in 0 until this.columns) {
                val value = this[i, j]
                if (value != null) {
                    newGrid[i, j] = value
                }
            }
        }
        return newGrid
    }


    fun getBottomRows(num: Int): Grid<T> {
        val newGrid = Grid<T>(num, this.columns)
        for (i in 0 ..< num)
            for (j in 0 ..< this.columns)
                newGrid[i,j] = this[this.rows - num + i,j]!!
        return newGrid
    }

    fun getLeftColumns(num: Int): Grid<T> {
        val newGrid = Grid<T>(this.rows, num)
        for (i in 0 ..< this.rows)
            for (j in 0 ..< num)
                newGrid[i, j] = this[i, j]!!
        return newGrid
    }

    fun getRightColumns(num: Int): Grid<T> {
        val newGrid = Grid<T>(this.rows, num)
        for (i in 0 ..< this.rows)
            for (j in 0 ..< num)
                newGrid[i, j] = this[i, this.columns - num + j]!!
        return newGrid
    }
    
    fun fillWith(value: T) {
        for (row in 0..<rows) {
            for (column in 0..<columns) {
                if (get(row, column) != null) continue
                set(row, column, value)
            }
        }
    }

    fun addPoints(xRange: IntRange, yRange: IntRange, value: T) {
        for (x in xRange) {
            for (y in yRange) {
                set(x, y, value)
            }
        }
    }

    fun invert(): Grid<T> {
        val newGrid = Grid<T>(columns, rows)

        for ((point, value) in this.grid) {
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



    override fun contains(element: T): Boolean = grid.containsValue(element)

    override val size: Int get() = grid.size

    override fun containsAll(elements: Collection<T>): Boolean = elements.all { this.contains(it) }

    override fun isEmpty(): Boolean = grid.isEmpty()

    override fun iterator(): Iterator<T> = grid.values.iterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid<*>

        if (rows != other.rows) return false
        if (columns != other.columns) return false
        if (grid != other.grid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        result = 31 * result + grid.hashCode()
        return result
    }

    fun deepCopy(): Grid<T> {
        val newGrid = Grid<T>(this.rows, this.columns)
        this.grid.forEach { (point, value) ->
            newGrid[point.x, point.y] = value
        }
        return newGrid
    }
}
