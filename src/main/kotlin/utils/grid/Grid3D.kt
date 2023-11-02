package utils.grid

import utils.point.Point3D

data class Grid3D<T>(var depth: Int = 0, var rows: Int = 0, var columns: Int = 0) : Collection<T> {
    val grid: MutableMap<Point3D, T> = mutableMapOf()

    operator fun set(points: Set<Point3D>, value: T) {
        for (point in points) this[point] = value
    }

    fun add(points: Set<Point3D>, value: T) {
        for (point in points) {
            if (this[point] == null) this[point] = value
        }
    }

    operator fun set(z: Int, x: Int, y: Int, value: T) {
        grid[Point3D(z, x, y)] = value
        depth = maxOf(depth, z + 1)
        rows = maxOf(rows, x + 1)
        columns = maxOf(columns, y + 1)
    }

    operator fun set(point: Point3D, value: T) {
        grid[point] = value
        depth = maxOf(depth, point.z + 1)
        rows = maxOf(rows, point.x + 1)
        columns = maxOf(columns, point.y + 1)
    }

    operator fun get(point: Point3D): T? = grid[point]
    operator fun get(z: Int, x: Int, y: Int): T? = grid[Point3D(z, x, y)]

    fun getOrDefault(point: Point3D, defaultValue: T): T = grid.getOrDefault(point, defaultValue)
    fun getOrDefault(value: T, defaultPoint: Point3D): Point3D =
        grid.entries.find { it.value == value }?.key ?: defaultPoint

    fun getPointsWithValue(value: T): Set<Point3D> = grid.filterValues { it == value }.keys.toSet()

    fun replace(oldValue: T, newValue: T) {
        val position = grid.entries.find { it.value == oldValue }?.key
        position?.let { grid[it] = newValue }
    }

    fun getCardinalNeighborPositions(z: Int, x: Int, y: Int): Set<Point3D> {
        val neighbors = mutableSetOf<Point3D>()
        val relativePositions = listOf(
            Point3D(0, -1, 0),
            Point3D(0, 0, -1),
            Point3D(0, 0, 1),
            Point3D(0, 1, 0),
            Point3D(-1, 0, 0),
            Point3D(1, 0, 0)
        )
        for (position in relativePositions) {
            val potentialNeighbor = Point3D(z + position.z, x + position.x, y + position.y)
            if (grid.containsKey(potentialNeighbor)) {
                neighbors.add(potentialNeighbor)
            }
        }
        return neighbors
    }

    fun getNeighbors(z: Int, x: Int, y: Int): Set<T?> = getNeighborPositions(z, x, y).map { get(it) }.toSet()
    fun getCardinalNeighbors(z: Int, x: Int, y: Int): Set<T?> =
        getCardinalNeighborPositions(z, x, y).map { get(it) }.toSet()

    fun getRelativePositions(): List<Point3D> {
        val relativePositions = mutableListOf<Point3D>()
        for (dz in -1..1) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    if (dz != 0 || dx != 0 || dy != 0) {
                        relativePositions.add(Point3D(dz, dx, dy))
                    }
                }
            }
        }
        return relativePositions
    }

    fun getNeighborPositions(z: Int, x: Int, y: Int): Set<Point3D> {
        val neighbors = mutableSetOf<Point3D>()
        val relativePositions = getRelativePositions()
        for (position in relativePositions) {
            val potentialNeighbor = Point3D(z + position.z, x + position.x, y + position.y)
            if (grid.containsKey(potentialNeighbor)) {
                neighbors.add(potentialNeighbor)
            }
        }
        return neighbors
    }

    fun movePointsBy(points: Set<Point3D>, dz: Int, dx: Int, dy: Int): Set<Point3D> {
        val newGrid = mutableMapOf<Point3D, T>()
        points.forEach { point ->
            val value = this.grid[point]
            if (value != null) {
                this.grid.remove(point)
                newGrid[Point3D(point.z + dz, point.x + dx, point.y + dy)] = value
            }
        }
        this.grid.putAll(newGrid)
        return newGrid.keys
    }

    fun getHighestOfValue(value: T): Point3D? = grid.filterValues { it == value }.keys.maxByOrNull { it.y }
    fun getLowestOfValue(value: T): Point3D? = grid.filterValues { it == value }.keys.minByOrNull { it.y }
    fun getRightmostOfValue(value: T): Point3D? = grid.filterValues { it == value }.keys.maxByOrNull { it.x }
    fun getLeftmostOfValue(value: T): Point3D? = grid.filterValues { it == value }.keys.minByOrNull { it.x }

    fun fillWith(value: T): Grid3D<T> {
        for (z in 0 ..< depth) {
            for (x in 0 ..< rows) {
                for (y in 0 ..< columns) {
                    if (get(z, x, y) == null) set(z, x, y, value)
                }
            }
        }
        return this
    }

    fun addPoints(zRange: IntRange, xRange: IntRange, yRange: IntRange, value: T) {
        for (z in zRange) {
            for (x in xRange) {
                for (y in yRange) {
                    set(z, x, y, value)
                }
            }
        }
    }

    fun isInside(z: Int, x: Int, y: Int): Boolean = z in 0 ..< depth && x in 0 ..< rows && y in 0 ..< columns
    fun isOutside(z: Int, x: Int, y: Int): Boolean = !isInside(z, x, y)

    fun canMove(points: Set<Point3D>, dz: Int, dx: Int, dy: Int): Boolean {
        return points.all { point ->
            val newZ = point.z + dz
            val newX = point.x + dx
            val newY = point.y + dy
            this[newZ, newX, newY] == null || Point3D(newZ, newX, newY) in points
        }
    }

    fun getExposedSides(point: Point3D): Int {
        var exposed = 6

        val neighbors = point.getCardinalNeighbors()

        for (neighbor in neighbors) {
            if (grid.containsKey(neighbor)) {
                exposed -= 1
            }
        }

        return exposed
    }

    override fun contains(element: T): Boolean = grid.containsValue(element)
    override val size: Int get() = grid.size
    override fun containsAll(elements: Collection<T>): Boolean = elements.all { this.contains(it) }
    override fun isEmpty(): Boolean = grid.isEmpty()
    override fun iterator(): Iterator<T> = grid.values.iterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid3D<*>

        if (depth != other.depth || rows != other.rows || columns != other.columns) return false

        for (z in 0 ..< depth) {
            for (x in 0 ..< rows) {
                for (y in 0 ..< columns) {
                    if (this[z, x, y] != other[z, x, y]) return false
                }
            }
        }

        return true
    }


    override fun hashCode(): Int {
        var result = depth
        result = 31 * result + rows
        result = 31 * result + columns
        result = 31 * result + grid.hashCode()
        return result
    }

    fun deepCopy(): Grid3D<T> {
        val newGrid = Grid3D<T>(this.depth, this.rows, this.columns)
        this.grid.forEach { (point, value) ->
            newGrid[point.z, point.x, point.y] = value
        }
        return newGrid
    }
}