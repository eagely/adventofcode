package utils.grid

import utils.point.Point3D

data class Grid3D<T>(var rows: Int = 0, var columns: Int = 0, var depths: Int = 0) : Collection<T> {
    private val grid: MutableMap<Point3D, T> = mutableMapOf()

    fun set(row: Int, column: Int, depth: Int, value: T) {
        grid[Point3D(row.toLong(), column.toLong(), depth.toLong())] = value

        rows = maxOf(rows, row + 1)
        columns = maxOf(columns, column + 1)
        depths = maxOf(depths, depth + 1)
    }

    fun getValue(row: Int, column: Int, depth: Int): T? = grid[Point3D(row.toLong(), column.toLong(), depth.toLong())]

    fun getPosition(value: T): Point3D {
        return grid.entries.find { it.value == value }?.key ?: throw Exception("Value not found")
    }

    fun replace(oldValue: T, newValue: T) {
        val position = getPosition(oldValue)
        position.let { grid[it] = newValue }
    }

    fun getLayer(depth: Int): List<List<T?>> {
        return (0 ..< rows).map { row ->
            (0 ..< columns).map { column ->
                getValue(row, column, depth)
            }
        }
    }

    fun getLayers(): List<List<List<T?>>> {
        return (0 ..< depths).map { depth ->
            getLayer(depth)
        }
    }

    fun getRow(row: Int, depth: Int): List<T?> = (0 ..< columns).map { getValue(row, it, depth) }
    fun getColumn(column: Int, depth: Int): List<T?> = (0 ..< rows).map { getValue(it, column, depth) }
    fun getDepthColumn(row: Int, column: Int): List<T?> = (0 ..< depths).map { getValue(row, column, it) }

    fun getNeighborPositions(row: Int, column: Int, depth: Int): List<Point3D> {
        val relativePositions = mutableListOf<Point3D>()
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    if (i != 0 || j != 0 || k != 0) {
                        relativePositions.add(Point3D(row.toLong() + i, column.toLong() + j, depth.toLong() + k))
                    }
                }
            }
        }
        return relativePositions.filter { it in grid }
    }

    fun getNeighbors(row: Int, column: Int, depth: Int): List<T?> {
        return getNeighborPositions(row, column, depth).map { grid[it] }
    }

    override fun contains(element: T): Boolean = grid.containsValue(element)
    override val size: Int get() = grid.size
    override fun containsAll(elements: Collection<T>): Boolean = elements.all { this.contains(it) }
    override fun isEmpty(): Boolean = grid.isEmpty()
    override fun iterator(): Iterator<T> = grid.values.iterator()
}
