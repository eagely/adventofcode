package utils.grid

import utils.Utils.abs
import utils.point.Point
import kotlin.math.max
import kotlin.math.sign

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
    operator fun get(value: T): Point =
        data.entries.find { it.value == value }?.key ?: throw Exception("Value not found")

    fun getOrDefault(point: Point, defaultValue: T): T = data.getOrDefault(point, defaultValue)
    fun getOrDefault(value: T, defaultPoint: Point): Point =
        data.entries.find { it.value == value }?.key ?: defaultPoint

    fun getPointsWithValue(value: T): Set<Point> = data.filterValues { it == value }.keys

    fun replace(oldValue: T, newValue: T): Grid<T> {
        val position = getPointsWithValue(oldValue)
        position.forEach { data[it] = newValue }
        return this
    }

    fun getRow(row: Int): List<T?> {
        val rowElements = mutableListOf<T?>()
        for (col in minY .. maxY) {
            rowElements.add(get(Point(row, col)))
        }
        return rowElements
    }

    fun getColumn(col: Int): List<T?> {
        val colElements = mutableListOf<T?>()
        for (row in minX .. maxX) {
            colElements.add(get(Point(row, col)))
        }
        return colElements
    }

    fun getRows(): List<List<T?>> {
        val rowsElements = mutableListOf<List<T?>>()
        for (row in minX .. maxX) {
            rowsElements.add(getRow(row))
        }
        return rowsElements
    }

    fun getColumns(): List<List<T?>> {
        val colsElements = mutableListOf<List<T?>>()
        for (col in minY .. maxY) {
            colsElements.add(getColumn(col))
        }
        return colsElements
    }

    fun getDiagonals(): List<List<T?>> {
        val diagonal1 = (0 ..< rows).map { get(Point(it, it)) }
        val diagonal2 = (0 ..< rows).map { get(Point(it, rows - it - 1)) }
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

    fun movePointsBy(
        points: Set<Point>,
        xOff: Int,
        yOff: Int,
        circular: Boolean = false,
        skipNull: Boolean = false
    ): Set<Point> {
        val newGrid = mutableMapOf<Point, T>()
        points.forEach { point ->
            val value = data[point]
            if (value != null) {
                var newX = point.x
                var newY = point.y
                while (skipNull && data[Point(newX, newY)] == null) {
                    newX = if (circular) (newX + xOff) % rows else newX + xOff
                    newY = if (circular) (newY + yOff) % columns else newY + yOff

                    if (!circular && (newX !in 0 until rows || newY !in 0 until columns)) {
                        newX = point.x
                        newY = point.y
                        break
                    }
                }
                data.remove(point)
                if (newX != point.x || newY != point.y) {
                    newGrid[Point(newX, newY)] = value
                } else if (circular) {
                    newGrid[Point((point.x + xOff) % rows, (point.y + yOff) % columns)] = value
                } else {
                    newGrid[Point(point.x + xOff, point.y + yOff)] = value
                }
            }
        }
        data.putAll(newGrid)
        return newGrid.keys
    }

    fun movePointBy(point: Point, xOff: Int, yOff: Int, circular: Boolean = false, skipNull: Boolean = false): Point {
        val value = data[point] ?: throw IllegalArgumentException("The point does not exist in the grid")
        var newX = point.x + xOff
        var newY = point.y + yOff
        if (skipNull) {
            while (data[Point(newX, newY)] == null) {
                newX = if (circular) (newX + 1).mod(rows) else newX + 1
                newY = if (circular) (newY + 1).mod(columns) else newY + 1

                if (newX == point.x && newY == point.y) throw IllegalArgumentException("No non-null points found in the given direction")
            }
        } else {
            if (circular) {
                newX = (newX + xOff).mod(rows)
                newY = (newY + yOff).mod(columns)
            } else {
                newX += xOff
                newY += yOff
            }
        }
        data.remove(point)
        val newPoint = Point(newX, newY)
        data[newPoint] = value

        return newPoint
    }

    fun getFirst(value: T): Point? {
        for (row in minX until maxX) {
            for (col in minY until maxY) {
                if (get(row, col) == value) {
                    return Point(row, col)
                }
            }
        }
        return null
    }

    fun getLast(value: T): Point? {
        for (row in maxX downTo minX) {
            for (col in maxY downTo minY) {
                if (get(row, col) == value) {
                    return Point(row, col)
                }
            }
        }
        return null
    }

    fun fillWith(value: T): Grid<T> {
        for (row in 0 ..< rows) {
            for (column in 0 ..< columns) {
                if (get(row, column) != null) continue
                set(row, column, value)
            }
        }
        return this
    }

    fun floodFill(startPoint: Point, newValue: T): Grid<T> {
        val oldValue =
            get(startPoint) ?: throw NullPointerException("Cannot flood fill null value, try using fillWith() first")

        val stack = mutableListOf(startPoint)

        while (stack.isNotEmpty()) {
            val currentPoint = stack.removeAt(stack.size - 1)

            if (currentPoint.x in 0 ..< rows && currentPoint.y in 0 ..< columns &&
                get(currentPoint) == oldValue
            ) {

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

        for ((point, value) in data) {
            val newPoint = Point(point.y, point.x)
            newGrid[newPoint] = value
        }

        return newGrid
    }

    fun isInside(x: Int, y: Int): Boolean {
        return x in 0 ..< rows && y in 0 ..< columns
    }

    fun isOutside(x: Int, y: Int): Boolean {
        return !(x in 0 ..< rows && y in 0 ..< columns)
    }

    fun canMove(points: Set<Point>, dx: Int, dy: Int): Boolean {
        return points.all { point ->
            val newX = point.x + dx
            val newY = point.y + dy
            this[newX, newY] == null || Point(newX, newY) in points
        }
    }

    fun subGrid(minrow: Int, maxrow: Int, mincol: Int, maxcol: Int): Grid<T> {
        val subGrid = Grid<T>(maxrow-minrow+1, maxcol-mincol+1)
        this.data.filter { (point, _) ->
            point.x in minrow..maxrow && point.y in mincol..maxcol
        }.forEach { (point, value) ->
            subGrid[Point(point.x - minrow, point.y - mincol)] = value
        }
        return subGrid
    }

    fun walledWrappedMove(point: Point, ro: Int, co: Int, wall: Char = '#'): Point {
        var (r, c) = point
        var pointBeforeWall = point
        repeat(ro.abs()) {
            r = (r + ro.sign) % rows
            var cell = this[Point(r, c)]
            if (cell == wall) {
                return pointBeforeWall
            }
            while (cell == null) {
                r = (r + ro.sign + rows) % rows
                cell = this[Point(r, c)]
            }
            if (cell == wall) {
                return pointBeforeWall
            }
            pointBeforeWall = Point(r, c)
        }
        repeat(co.abs()) {
            c = (c + co.sign) % columns
            var cell = this[Point(r, c)]
            if (cell == wall) {
                return pointBeforeWall
            }
            while (cell == null) {
                c = (c + co.sign + columns) % columns
                cell = this[Point(r, c)]
            }
            if (cell == wall) {
                return pointBeforeWall
            }
            pointBeforeWall = Point(r, c)
        }
        return Point(r, c)
    }

    fun oldToString(): String {
            val stringBuilder = StringBuilder()
            val defaultFill = " "
            val yList = (minY..maxY).toList()
            val i = yList.map { it.toString() }.maxOf { it.length }
            val curPrint = i
            for(j in i-1 downTo 0) {
                stringBuilder.append(" ".repeat(i - j))
                yList.forEach { stringBuilder.append(try { it.toString()[curPrint] } catch (e: StringIndexOutOfBoundsException) { ' ' } + " ") }
                stringBuilder.append("\n")
            }

            (minX..maxX).forEach { row ->
                (minY..maxY).forEach { col ->
                    val value = get(Point(row, col))
                    stringBuilder.append(value?.toString() ?: defaultFill)
                }
                stringBuilder.append("\n")
            }

            return stringBuilder.toString()
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()

        val maxDimension = max(maxX, maxY)

        val topIndices = (minX..maxDimension).map {
            if (it < 10) {
                " "
            } else {
                it / 10
            }
        }.joinToString(separator = " ")

        val bottomIndices = (minX..maxDimension).map { it % 10 }.joinToString(separator = " ")

        // Add the column indices to the string
        stringBuilder.append("$topIndices\n$bottomIndices\n")

        // print grid values
        (minY..maxDimension).forEach { row ->  // reverse order of rows
            (minX..maxDimension).forEach { col ->
                val value = data[Point(row, col)]?.toString() ?: " "
                stringBuilder.append("$value ")
            }
            if (row < maxDimension) stringBuilder.append("\n")
        }

        return stringBuilder.toString()
    }

    override fun contains(element: T): Boolean = data.containsValue(element)

    fun contains(element: Point): Boolean = data.containsKey(element)

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
        data.forEach { (point, value) ->
            newGrid[point.x, point.y] = value
        }
        return newGrid
    }

    companion object {
        fun <T> of(rows: List<List<T>>): Grid<T?> {
            val grid = Grid<T?>()
            for (i in rows.indices) {
                for (j in rows[i].indices) {
                    if (rows[i][j] != ' ')
                        grid[Point(i, j)] = rows[i][j]
                }
            }
            return grid
        }

        @JvmName("ofSingleList")
        fun <T : Any> of(rows: List<T>): Grid<T?> {
            val grid = Grid<T?>()
            rows.forEachIndexed { i, value ->
                grid[Point(i, 0)] = value
            }
            return grid
        }

        @JvmName("ofStringList")
        fun of(rows: List<String>): Grid<Char> {
            val grid = Grid<Char>()
            for (i in rows.indices) {
                for (j in rows[i].indices) {
                    if (rows[i][j] != ' ')
                        grid[Point(i, j)] = rows[i][j]
                }
            }
            return grid
        }
    }
}
