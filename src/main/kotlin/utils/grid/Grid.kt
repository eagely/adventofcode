package utils.grid

import utils.point.Point

data class Grid<T>(var rows: Int = 0, var columns: Int = 0) : Collection<T> {
    val grid: MutableMap<Point, T> = mutableMapOf()

    fun set(row: Int, column: Int, value: T) {
        grid[Point(row.toLong(), column.toLong())] = value

        rows = maxOf(rows, row + 1)
        columns = maxOf(columns, column + 1)
    }


    fun getValue(row: Int, column: Int): T? = grid[Point(row.toLong(), column.toLong())]

    fun getPosition(value: T): Point {
        return grid.entries.find { it.value == value }?.key ?: throw Exception("Value not found")
    }

    fun replace(oldValue: T, newValue: T) {
        val position = getPosition(oldValue)
        position.let { grid[it] = newValue }
    }

    fun getRow(row: Int): List<T?> = (0..<columns).map { getValue(row, it) }

    fun getRows(): List<List<T?>> = (0..<rows).map { getRow(it) }

    fun getColumn(column: Int): List<T?> = (0..<rows).map { getValue(it, column) }

    fun getColumns(): List<List<T?>> = (0..<columns).map { getColumn(it) }

    fun getDiagonals(): List<List<T?>> {
        val diagonal1 = (0..<rows).map { getValue(it, it) }
        val diagonal2 = (0..<rows).map { getValue(it, rows - it - 1) }
        return listOf(diagonal1, diagonal2)
    }

    fun getNeighborPositions(row: Int, column: Int, includeDiagonal: Boolean = true): List<Point> {
        val neighbors = mutableListOf<Point>()

        val relativePositions = mutableListOf(
            Point(-1, 0), Point(0, -1), /* skip self */ Point(0, 1), Point(1, 0)
        )

        if (includeDiagonal) {
            relativePositions.addAll(
                listOf(
                    Point(-1, -1), Point(-1, 1), Point(1, -1), Point(1, 1)
                )
            )
        }

        for (position in relativePositions) {
            val potentialNeighbor = Point(row.toLong() + position.x.toLong(), column.toLong() + position.y.toLong())
            if (grid.containsKey(potentialNeighbor)) {
                neighbors.add(potentialNeighbor)
            }
        }

        return neighbors
    }

    fun getNeighbors(row: Int, column: Int, includeDiagonal: Boolean = true): List<T?> {
        return getNeighborPositions(row, column, includeDiagonal).map { getValue(it.x.toInt(), it.y.toInt()) }
    }

    override fun toString(): String {
        val rowsInGrid = grid.keys.maxOfOrNull { it.x.toInt() }?.let { 0..it } ?: return "[]"
        val columnsInGrid = grid.keys.maxOfOrNull { it.y.toInt() }?.let { 0..it } ?: return "[]"

        return rowsInGrid.joinToString(prefix = "[", postfix = "]") { row ->
            columnsInGrid.joinToString(prefix = "[", postfix = "]", separator = ", ") { column ->
                getValue(row, column).toString()
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
