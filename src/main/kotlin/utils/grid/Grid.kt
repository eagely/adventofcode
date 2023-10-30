package utils.grid

import utils.point.Point

data class Grid<T>(var rows: Int = 0, var columns: Int = 0) : Collection<T> {
    val grid: MutableMap<Point, T> = mutableMapOf()

    val minX: Int get() = grid.keys.minOf { it.x }
    val minY: Int get() = grid.keys.minOf { it.y }
    val maxX: Int get() = grid.keys.maxOf { it.x }
    val maxY: Int get() = grid.keys.maxOf { it.y }
    val min: Point get() = Point(minX, minY)
    val max: Point get() = Point(maxX, maxY)
    operator fun set(row: Int, column: Int, value: T) {
        grid[Point(row.toLong(), column.toLong())] = value

        rows = maxOf(rows, row + 1)
        columns = maxOf(columns, column + 1)
    }

    operator fun set(point: Point, value: T) {
        grid[point] = value
        rows = maxOf(rows, point.x + 1)
        columns = maxOf(columns, point.y + 1)
    }

    operator fun get(point: Point): T? {
        return grid[point]
    }

    operator fun get(x: Int, y: Int): T? {
        return grid[Point(x, y)]
    }

    fun getOrDefault(point: Point, defaultValue: T): T {
        return grid.getOrDefault(point, defaultValue)
    }

    operator fun get(value: T): Point {
        return grid.entries.find { it.value == value }?.key ?: throw Exception("Value not found")
    }

    fun getOrDefault(value: T, defaultPoint: Point): Point {
        return grid.entries.find { it.value == value }?.key ?: defaultPoint

    }

    fun replace(oldValue: T, newValue: T) {
        val position = get(oldValue)
        position.let { grid[it] = newValue }
    }

    fun getRow(index: Int): List<T?> = (0..<columns).map { get(Point(index, it)) }

    fun getRows(): List<List<T?>> = (0..<rows).map { getRow(it) }

    fun getColumn(index: Int): List<T?> = (0..<rows).map { get(Point(it, index)) }

    fun getColumns(): List<List<T?>> = (0..<columns).map { getColumn(it) }

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
            val potentialNeighbor = Point(row.toLong() + position.x.toLong(), column.toLong() + position.y.toLong())
            if (grid.containsKey(potentialNeighbor)) {
                neighbors.add(potentialNeighbor)
            }
        }

        return neighbors
    }

    fun getNeighborPositions(row: Int, column: Int): Set<Point> {
        val neighbors = mutableSetOf<Point>()

        val relativePositions = listOf(
            Point(-1, -1), Point(-1, 0), Point(-1, 1), Point(0, -1),
            Point(0, 1), Point(1, -1), Point(1, 0), Point(1, 1)
        )

        for (position in relativePositions) {
            val potentialNeighbor = Point(row.toLong() + position.x.toLong(), column.toLong() + position.y.toLong())
            if (grid.containsKey(potentialNeighbor)) {
                neighbors.add(potentialNeighbor)
            }
        }

        return neighbors
    }

    fun getNeighbors(row: Int, column: Int): Set<T?> {
        return getNeighborPositions(row, column).map { get(Point(it.x, it.y)) }.toSet()
    }

    fun getCardinalNeighbors(row: Int, column: Int): Set<T?> {
        return getCardinalNeighborPositions(row, column).map { get(Point(it.x, it.y)) }.toSet()
    }


    fun fillWith(value: T) {
        for (row in 0..<rows) {
            for (column in 0..<columns) {
                if(get(row, column) != null) continue
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

    override fun toString(): String {
        val rowsInGrid = grid.keys.maxOfOrNull { it.x }?.let { 0..it } ?: return "[]"
        val columnsInGrid = grid.keys.maxOfOrNull { it.y }?.let { 0..it } ?: return "[]"

        // Use lineSeparator to create new row for each row
        val lineSeparator = System.lineSeparator()
        return rowsInGrid.joinToString(separator = lineSeparator) { row ->
            // Join elements of a row with " " (space), it will create a space-separated row
            columnsInGrid.joinToString(separator = " ") { column ->
                get(Point(row, column))?.toString() ?: " "
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

}
