package utils.grid

import utils.Utils.abs
import utils.Utils.cardinalDirections
import utils.Utils.directions
import utils.Utils.p
import utils.point.Point
import kotlin.math.sign

/**
 * A generic dynamic grid implementation.
 * It uses Map<Point, T> to store the points, which means accessing a known point is O(1) but indexing a point is O(n).
 * Hence, there is no guarantee that the points will be in any particular order.
 * Currently, since Point3D stores Ints, the grid is limited to 2^32 rows and columns.
 * @param T the type of elements in the grid.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Grid<T>(val initialRows: Int, val initialColumns: Int) : Collection<T> {
    constructor() : this(0, 0)

    /**
     * The indices of the grid
     * @return a list of all the indices in the grid.
     */
    val indices: List<Point> get() = data.keys.toList()

    /**
     * The data of the grid, stored as a map of points to values.
     */
    var data = mutableMapOf<Point, T>()

    /**
     * The number of elements in the grid.
     */
    override val size: Int get() = data.size

    /**
     * The rows of the grid.
     */
    val rows: Int get() = maxX - minX + 1

    /**
     * The columns of the grid.
     */
    val columns: Int get() = maxY - minY + 1

    /**
     * The lowest x value in the grid.
     */
    val minX: Int get() = data.keys.minOfOrNull { it.x } ?: 0

    /**
     * The lowest y value in the grid.
     */
    val minY: Int get() = data.keys.minOfOrNull { it.y } ?: 0

    /**
     * The highest x value in the grid.
     */
    val maxX: Int get() = data.keys.maxOfOrNull { it.x } ?: 0

    /**
     * The highest y value in the grid.
     */
    val maxY: Int get() = data.keys.maxOfOrNull { it.y } ?: 0

    /**
     * Sets all the points in the set to the given value.
     * @param points the points to set.
     * @param value the value to set the points to.
     */
    operator fun set(points: Set<Point>, value: T) {
        points.forEach { this[it] = value }
    }

    /**
     * Adds all the points in the set if they are not present in the grid and sets them to the given value.
     * @param points the points to add.
     * @param value the value to set the points to.
     */
    fun add(points: Set<Point>, value: T) {
        points.forEach { point -> data.putIfAbsent(point, value) }
    }

    /**
     * Sets the point at the specified Point to the given value.
     * @param point the point to set.
     * @param value the value to set the point to.
     */
    operator fun set(point: Point, value: T) {
        data[point] = value
    }

    /**
     * Sets the points in the specified rectangle to the given value.
     * @param xRange the range of x values to set.
     * @param yRange the range of y values to set.
     * @param value the value to set the points to.
     */
    operator fun set(xRange: IntRange, yRange: IntRange, value: T) = xRange.forEach { x -> yRange.forEach { y -> set(x p y, value) } }

    /**
     * Sets the points in the specified vertical line to the given value.
     * @param x the x value of the column.
     * @param yRange the range of y values to set.
     * @param value the value to set the points to.
     */
    operator fun set(x: Int, yRange: IntRange, value: T) = yRange.forEach { set(x p it, value) }

    /**
     * Sets the points in the specified horizontal line to the given value.
     * @param xRange the range of x values to set.
     * @param y the y value of the row.
     * @param value the value to set the points to.
     */
    operator fun set(xRange: IntRange, y: Int, value: T) = xRange.forEach { set(it p y, value) }

    /**
     * Returns the value at the specified point.
     * @param point the point to get the value of.
     * @return the value at the specified point, or null if absent.
     */
    operator fun get(point: Point): T? = data[point]

    /**
     * Returns the first point with the specified value.
     * @param value the value to search for.
     * @return the first point with the specified value, or null if absent.
     */
    operator fun get(value: T): Point? = data.entries.find { it.value == value }?.key

    /**
     * Returns the value at the specified Point, or defaultValue if absent.
     * @param point the point to get the value of.
     * @param defaultValue the value to return if the point is absent.
     * @return the value at the specified point, or defaultValue if absent.
     */
    fun getOrDefault(point: Point, defaultValue: T) = data.getOrDefault(point, defaultValue)

    /**
     * Returns the first point with the specified value, or defaultPoint if absent.
     * @param value the value to search for.
     * @param defaultPoint the point to return if the value is absent.
     * @return the first point with the specified value, or defaultPoint if absent.
     */
    fun getOrDefault(value: T, defaultPoint: Point) = data.entries.find { it.value == value }?.key ?: defaultPoint

    /**
     * Returns a Set of all points with the specified value.
     * @param value the value to search for.
     * @return a Set of all points with the specified value.
     */
    fun getPointsWithValue(value: T) = data.filterValues { it == value }.keys

    /**
     * Returns the specified row.
     * @param row the row to get.
     * @return the specified row.
     */
    fun getRow(row: Int) = data.filter { it.key.x == row }.values.toList()

    /**
     * Returns the specified column.
     * @param col the column to get.
     * @return the specified column.
     */
    fun getColumn(col: Int) = data.filter { it.key.y == col }.values.toList()

    /**
     * Returns a list of all rows.
     * @return a list of all rows.
     */
    fun getRows(): List<List<T?>> = (minX..maxX).map { row -> getRow(row) }

    /**
     * Returns a list of all columns.
     * @return a list of all columns.
     */
    fun getColumns(): List<List<T?>> = (minY..maxY).map { col -> getColumn(col) }

    /**
     * Returns a list of all sides of the grid.
     * @return a list of all sides of the grid.
     */
    fun getSides(): List<List<T?>> = listOf(getRow(minX), getRow(maxX), getColumn(minY), getColumn(maxY))

    /**
     * Iterates over all the points in the grid.
     * @param action the action to perform on each point.
     */
    fun forEach(action: (T) -> Unit) {
        data.values.forEach(action)
    }

    /**
     * Iterates over all the points in the grid with their points as indices.
     * @param action the action to perform on each point.
     */
    fun forEachIndexed(action: (Point, T) -> Unit) {
        data.forEach { (point, value) -> action(point, value) }
    }

    /**
     * Maps all points in the grid to a specified transform.
     * @param transform the transform to apply to each point.
     * @return a new shallow copy grid containing the mapped points.
     */
    fun <R> map(transform: (T) -> R) = Grid<R>().apply { data = this@Grid.data.mapValues { transform(it.value) }.toMutableMap() }

    /**
     * Maps all points in the grid to a specified transform with their points as indices.
     * @param transform the transform to apply to each point.
     * @return a new shallow copy grid containing the mapped points.
     */
    fun <R> mapIndexed(transform: (Point, T) -> R) = Grid<R>().apply { data = this@Grid.data.mapValues { (p, v) -> transform(p, v) }.toMutableMap() }

    /**
     * Filters all points in the grid to those that match the specified predicate.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy grid containing the filtered points.
     */
    fun filter(predicate: (T) -> Boolean) = Grid<T>().apply { data = this@Grid.data.filterValues(predicate).toMutableMap() }

    /**
     * Filters all points in the grid to those that match the specified predicate with their points as indices.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy grid containing the filtered points.
     */
    fun filterIndexed(predicate: (Point, T) -> Boolean) = Grid<T>().apply { data.filter { (point, value) -> predicate(point, value) }.values.toList() }

    /**
     * Filters all points in the grid to those that match the specified predicate, but keeps consecutively matching points together.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy Grid<String> containing the filtered points, with each string being a consecutive sequence of matching points.
     */
    fun filterConsecutive(predicate: (T) -> Boolean): Grid<String> {
        val result = data.entries.fold(mutableListOf<Pair<Point, StringBuilder>>()) { acc, (point, value) ->
            if (predicate(value)) {
                if (acc.isEmpty() || point != acc.last().first + Point(0, 1)) {
                    acc.add(point to StringBuilder(value.toString()))
                } else {
                    acc.last().second.append(value)
                }
            }
            acc
        }
        val resultGrid = Grid<String>()
        result.forEach { (startPoint, stringBuilder) ->
            resultGrid[startPoint] = stringBuilder.toString()
        }
        return resultGrid
    }

    /**
     * Returns true if any elements in the grid match the given predicate.
     * @param predicate the predicate to match.
     * @return true if any elements in the grid match the given predicate.
     */
    fun any(predicate: (T) -> Boolean): Boolean {
        return data.values.any(predicate)
    }

    /**
     * Returns true if any elements in the grid match the given predicate with their points as indices.
     * @param predicate the predicate to match.
     * @return true if any elements in the grid match the given predicate.
     */
    fun anyIndexed(predicate: (Point, T) -> Boolean): Boolean {
        return data.any { (point, value) -> predicate(point, value) }
    }

    /**
     * Returns true if all elements in the grid match the given predicate.
     * @param predicate the predicate to match.
     * @return true if all elements in the grid match the given predicate.
     */
    fun all(predicate: (T) -> Boolean): Boolean {
        return data.values.all(predicate)
    }

    /**
     * Returns true if all elements in the grid match the given predicate with their points as indices.
     * @param predicate the predicate to match.
     * @return true if all elements in the grid match the given predicate.
     */
    fun allIndexed(predicate: (Point, T) -> Boolean): Boolean {
        return data.all { (point, value) -> predicate(point, value) }
    }

    /**
     * Returns true if none of the elements in the grid match the given predicate.
     * @param predicate the predicate to match.
     * @return true if none of the elements in the grid match the given predicate.
     */
    fun none(predicate: (T) -> Boolean): Boolean {
        return data.values.none(predicate)
    }

    /**
     * Returns true if none of the elements in the grid match the given predicate with their points as indices.
     * @param predicate the predicate to match.
     * @return true if none of the elements in the grid match the given predicate.
     */
    fun noneIndexed(predicate: (Point, T) -> Boolean): Boolean {
        return data.none { (point, value) -> predicate(point, value) }
    }

    /**
     * Returns a Set of all points that are diagonal or cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a Set of all points that are diagonal or cardinal neighbors of the specified point and are in the grid.
     */
    fun getNeighborPositions(point: Point) = point.getNeighbors().filter { it in data.keys }

    /**
     * Returns a List of all values that are diagonal or cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a List of all values that are diagonal or cardinal neighbors of the specified point and are in the grid.
     */
    fun getNeighbors(point: Point) = getNeighborPositions(point).map { get(it) }

    /**
     * Returns a Set of all points that are cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a Set of all points that are cardinal neighbors of the specified point and are in the grid.
     */
    fun getCardinalNeighborPositions(point: Point) = point.getCardinalNeighbors().filter { it in data.keys }

    /**
     * Returns a List of all values that are cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a List of all values that are cardinal neighbors of the specified point and are in the grid.
     */
    fun getCardinalNeighbors(point: Point) = getCardinalNeighborPositions(point).mapNotNull { get(it) }

    /**
     * Returns a List of all values that are diagonal or cardinal neighbors of the specified point and are not the specified value.
     * It keeps moving in each direction until it finds a non-skipped neighbor or reaches the grid boundary.
     * @param point the point to get the neighbors of.
     * @param skipValue the value to skip.
     * @return a List of all values that are diagonal or cardinal neighbors of the specified point and are not the specified value.
     */
    fun getNeighbors(point: Point, skipValue: T): List<T?> {
        return directions().mapNotNull { direction ->
            var nextPoint = point + direction
            while (nextPoint in data.keys && get(nextPoint) == skipValue) {
                nextPoint += direction
            }
            get(nextPoint)
        }
    }

    /**
     * Returns a List of all values that are cardinal neighbors of the specified point and are not the specified value.
     * It keeps moving in each direction until it finds a non-skipped neighbor or reaches the grid boundary.
     * @param point the point to get the neighbors of.
     * @param skipValue the value to skip.
     * @return a List of all values that are cardinal neighbors of the specified point and are not the specified value.
     */
    fun getCardinalNeighbors(point: Point, skipValue: T): List<T?> {
        return cardinalDirections().mapNotNull { direction ->
            var nextPoint = point + direction
            while (nextPoint in data.keys && get(nextPoint) == skipValue) {
                nextPoint += direction
            }
            get(nextPoint)
        }
    }

    /**
     * Returns a Set of points after moving the specified Set of points in the given direction.
     * This method is non local and will affect the grid.
     * @param points the points to move.
     * @param dx the x offset to move by.
     * @param dy the y offset to move by.
     * @param circular whether to wrap around the grid.
     * @param skipNull whether to skip null points.
     * @return a Set of points after moving the specified Set of points in the given direction.
     */
    fun movePointsBy(points: Set<Point>, dx: Int, dy: Int, circular: Boolean = false, skipNull: Boolean = false): Set<Point> {
        val newGrid = mutableMapOf<Point, T>()
        points.forEach { point ->
            val value = data[point]
            if (value != null) {
                var newX = point.x
                var newY = point.y
                while (skipNull && data[Point(newX, newY)] == null) {
                    newX = if (circular) (newX + dx) % rows else newX + dx
                    newY = if (circular) (newY + dy) % columns else newY + dy

                    if (!circular && (newX !in 0..<rows || newY !in 0..<columns)) {
                        newX = point.x
                        newY = point.y
                        break
                    }
                }
                data.remove(point)
                if (newX != point.x || newY != point.y) {
                    newGrid[Point(newX, newY)] = value
                } else if (circular) {
                    newGrid[Point((point.x + dx) % rows, (point.y + dy) % columns)] = value
                } else {
                    newGrid[Point(point.x + dx, point.y + dy)] = value
                }
            }
        }
        data.putAll(newGrid)
        return newGrid.keys
    }

    /**
     * Returns a Point after moving the specified Point in the given direction.
     * This method is non local and will affect the grid.
     * @param point the point to move.
     * @param dx the x offset to move by.
     * @param dy the y offset to move by.
     * @param circular whether to wrap around the grid.
     * @param skipNull whether to skip null points.
     * @return a Point after moving the specified Point in the given direction.
     */
    fun movePointBy(point: Point, dx: Int, dy: Int, circular: Boolean = false, skipNull: Boolean = false): Point {
        val value = data[point] ?: throw IllegalArgumentException("The point does not exist in the grid")
        var newX = point.x + dx
        var newY = point.y + dy
        if (skipNull) {
            while (data[Point(newX, newY)] == null) {
                newX = if (circular) (newX + 1).mod(rows) else newX + 1
                newY = if (circular) (newY + 1).mod(columns) else newY + 1

                if (newX == point.x && newY == point.y) throw IllegalArgumentException("No non-null points found in the given direction")
            }
        } else {
            if (circular) {
                newX = (newX + dx).mod(rows)
                newY = (newY + dy).mod(columns)
            } else {
                newX += dx
                newY += dy
            }
        }
        data.remove(point)
        val newPoint = Point(newX, newY)
        data[newPoint] = value
        return newPoint
    }

    fun rotateClockwise(): Grid<T> {
        val newGrid = Grid<T>(initialColumns, initialRows)
        forEachIndexed { point, value ->
            newGrid[Point(point.y, initialRows - 1 - point.x)] = value
        }
        return newGrid
    }

    fun flipHorizontal(): Grid<T> {
        val newGrid = Grid<T>(initialRows, initialColumns)
        forEachIndexed { point, value ->
            newGrid[Point(initialRows - 1 - point.x, point.y)] = value
        }
        return newGrid
    }

    /**
     * Replaces all null points in the grid with the given value, turning the grid into a rectangle.
     * This method is non local and will affect the grid.
     * @param value the value to replace null points with.
     * @return the grid after replacing all null points with the given value.
     */
    fun fillWith(value: T): Grid<T> {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                if (data[Point(x, y)] == null) {
                    data[Point(x, y)] = value
                }
            }
        }
        return this
    }

    /**
     * Flood fills all points cardinally (4-way) connected to startPoint with the specified newValue.
     * This method is non local and will affect the grid.
     * @param startPoint the point to start the flood fill from.
     * @param newValue the value to set all points to.
     * @return the grid after flood filling.
     */
    fun floodFill(startPoint: Point, newValue: T): Grid<T> {
        val originalValue = get(startPoint) ?: return this
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque<Point>()
        queue.add(startPoint)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current in visited || get(current) != originalValue) continue
            this[current] = newValue
            visited.add(current)
            queue.addAll(current.getCardinalNeighbors())
        }
        return this
    }

    /**
     * Returns true if the endPoint is cardinally (4-way) connected to startPoint by a flood fill.
     * @param startPoint the point to start the flood fill from.
     * @param endPoint the point to check if it is connected to startPoint.
     * @return true if the endPoint is connected to startPoint by a flood fill.
     */
    fun floodFillContains(startPoint: Point, endPoint: Point): Boolean {
        val match = get(startPoint) ?: return false
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque<Point>()
        queue.add(startPoint)

        while (queue.isNotEmpty()) {
            val currentPoint = queue.removeFirst()
            if (currentPoint == endPoint) return true
            if (!visited.add(currentPoint)) continue
            if (currentPoint.x !in minX..maxX || currentPoint.y !in minY..maxY || get(currentPoint) != match) continue
            queue.addAll(currentPoint.getCardinalNeighbors().filter { it !in visited })
        }
        return false
    }

    /**
     * Returns true if all the points in the Set can move by the specified offset.
     * A point being able to move means the point after the offset is either null or in the Set.
     * Points between the original point and the point after the offset are ignored.
     * @param points the points to check if they can move.
     * @param dx the x offset to move by.
     * @param dy the y offset to move by.
     * @param free the points to ignore when checking if the points can move.
     * @return true if all the points in the Set can move by the specified offset.
     */
    fun canMove(points: Set<Point>, dx: Int, dy: Int, vararg free: Point?) = points.all { point ->
        val np = point + Point(dx, dy)
        if (free.isEmpty()) this[np] == null else np in free || np in points
    }

    /**
     * Returns a subgrid within the specified bounds.
     * @param minrow the minimum row of the subgrid.
     * @param maxrow the maximum row of the subgrid.
     * @param mincol the minimum column of the subgrid.
     * @param maxcol the maximum column of the subgrid.
     * @return a subgrid within the specified bounds.
     */
    fun subGrid(minrow: Int, maxrow: Int, mincol: Int, maxcol: Int): Grid<T> {
        val subGrid = Grid<T>(maxrow - minrow + 1, maxcol - mincol + 1)
        this.data.filter { (point, _) ->
            point.x in minrow..maxrow && point.y in mincol..maxcol
        }.forEach { (point, value) ->
            subGrid[Point(point.x - minrow, point.y - mincol)] = value
        }
        return subGrid
    }

    /**
     * Moves the specified point by the given offset, wrapping around the grid if necessary, stops if it hits a wall.
     * @param point the point to move.
     * @param dx the x offset to move by.
     * @param dy the y offset to move by.
     * @param wall the character to stop at.
     * @return the point after moving.
     */
    fun walledWrappedMove(point: Point, dx: Int, dy: Int, wall: Char = '#'): Point {
        var (r, c) = point
        var pointBeforeWall = point
        repeat(dx.abs()) {
            r = (r + dx.sign) % rows
            var cell = this[Point(r, c)]
            if (cell == wall) {
                return pointBeforeWall
            }
            while (cell == null) {
                r = (r + dx.sign + rows) % rows
                cell = this[Point(r, c)]
            }
            if (cell == wall) {
                return pointBeforeWall
            }
            pointBeforeWall = Point(r, c)
        }
        repeat(dy.abs()) {
            c = (c + dy.sign) % columns
            var cell = this[Point(r, c)]
            if (cell == wall) {
                return pointBeforeWall
            }
            while (cell == null) {
                c = (c + dy.sign + columns) % columns
                cell = this[Point(r, c)]
            }
            if (cell == wall) {
                return pointBeforeWall
            }
            pointBeforeWall = Point(r, c)
        }
        return Point(r, c)
    }

    /**
     * Returns a String representation of the grid.
     * @return a String representation of the grid.
     */
    override fun toString(): String {
        val stringBuilder = StringBuilder()
        val defaultFill = " "
        val yList = (minY..maxY).toList()
        val i = yList.map { it.toString() }.maxOf { it.length }
        for (j in i - 1 downTo 0) {
            stringBuilder.append(" ".repeat(i - j))
            yList.forEach {
                stringBuilder.append(
                    try {
                        it.toString()[i]
                    } catch (e: StringIndexOutOfBoundsException) {
                        ' '
                    } + " "
                )
            }
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

    /**
     * Returns true if the grid contains the specified value.
     * @param element the value to search for.
     * @return true if the grid contains the specified value.
     */
    override fun contains(element: T): Boolean = data.containsValue(element)

    /**
     * Returns true if the grid contains the specified point.
     * @param element the point to search for.
     * @return true if the grid contains the specified point.
     */
    fun contains(element: Point): Boolean = data.containsKey(element)

    /**
     * Returns true if the grid contains all the elements in the specified collection.
     * @param elements the collection of elements to search for.
     * @return true if the grid contains all the elements in the specified collection.
     */
    override fun containsAll(elements: Collection<T>): Boolean = elements.all { this.contains(it) }

    /**
     * Returns true if the grid is empty.
     * @return true if the grid is empty.
     */
    override fun isEmpty(): Boolean = data.isEmpty()

    /**
     * Returns true if the grid is not empty.
     * @return true if the grid is not empty.
     */
    fun isNotEmpty(): Boolean = data.isNotEmpty()

    /**
     * Returns an iterator over the values of the grid.
     * @return an iterator over the values of the grid.
     */
    override fun iterator(): Iterator<T> = data.values.iterator()

    /**
     * Compares the specified object with this grid for equality.
     * Equality means the other object is a grid with all the same points and values.
     * @param other the object to compare with.
     * @return true if the specified object is equal to this grid.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid<*>

        return data == other.data
    }

    /**
     * Returns a hash code value for the grid.
     * @return a hash code value for the grid.
     */
    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        result = 31 * result + data.hashCode()
        return result
    }

    /**
     * Returns a deep copy of the grid.
     * @return a deep copy of the grid.
     */
    fun deepCopy(copyFunction: (T) -> T): Grid<T> {
        val newData = mutableMapOf<Point, T>()
        for ((point, value) in this.data) {
            newData[point.copy()] = copyFunction(value)
        }
        return Grid<T>(rows, columns).apply { this.data = newData }
    }

    /**
     * The following methods are deprecated and are only kept because they are used in some of my solutions.
     * They may be removed at any point, do not use them.
     */

    /**
     * Returns the first point with the specified value.
     * @param value the value to search for.
     * @return the first point with the specified value, or null if absent.
     */
    @Deprecated("use grid[value] instead", ReplaceWith("get(value)"))
    fun getFirst(value: T): Point? {
        for (row in minX..maxX) {
            for (col in minY..maxY) {
                val point = Point(row, col)
                if (data[point] == value) {
                    return point
                }
            }
        }
        return null
    }

    companion object {
        /**
         * Deep copies the Grid<Char>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyChar")
        fun Grid<Char>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid<Int>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyInt")
        fun Grid<Int>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid<Boolean>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyBoolean")
        fun Grid<Boolean>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid<String>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyString")
        fun Grid<String>.deepCopy() = of(this.data.map { it.key.copy() to String(it.value.toCharArray()) }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid<Double>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyDouble")
        fun Grid<Double>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid<Float>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyFloat")
        fun Grid<Float>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid<Long>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyLong")
        fun Grid<Long>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid<Short>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyShort")
        fun Grid<Short>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid<Byte>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyByte")
        fun Grid<Byte>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Create a new grid from the specified mutable map data.
         * @param data the data to create the grid from.
         * @return a new grid from the specified mutable map data.
         */
        @JvmName("ofMutableMap")
        fun <T> of(data: MutableMap<Point, T>): Grid<T> = Grid<T>().apply {
            this.data = data
        }

        /**
         * Create a new grid from the specified map data.
         * @param data the data to create the grid from.
         * @return a new grid from the specified map data.
         */
        @JvmName("ofMap")
        fun <T> of(data: Map<Point, T>): Grid<T> = Grid<T>().apply {
            this.data = data.toMutableMap()
        }

        /**
         * Create a new grid from the specified list of rows where each row is a list of T.
         * @param rows the rows to create the grid from.
         * @return a new grid from the specified rows.
         */
        @JvmName("ofDoubleList")
        fun <T> of(rows: List<List<T>>): Grid<T> {
            val data = rows.flatMapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, element ->
                    Point(rowIndex, colIndex) to element
                }
            }.toMap().toMutableMap()
            return Grid<T>().apply {
                this.data = data

            }
        }

        /**
         * Creates a Grid<Char> from the specified list of rows where each row is a String.
         * @param rows the rows to create the grid from.
         * @return a new grid from the specified rows.
         */
        @JvmName("ofStringList")
        fun of(rows: List<String>): Grid<Char> {
            val data = rows.flatMapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, element ->
                    Point(rowIndex, colIndex) to element
                }
            }.filter { it.second != ' ' }.toMap().toMutableMap()
            return Grid<Char>().apply {
                this.data = data
            }
        }

        /**
         * Returns the number at the specified point, a number can go multiple digits in either horizontal direction.
         * @param point the point to get the number of.
         * @return the number at the specified point, or null if absent.
         */
        @JvmName("numberAt")
        fun Grid<Char>.numberAt(point: Point): String? {
            if (get(point) == null || !get(point)!!.isDigit()) return null
            var start = point.y
            while (start >= 0 && this[Point(point.x, start)]!!.isDigit()) {
                start--
            }
            var end = point.y
            while (end < columns && this[Point(point.x, end)]!!.isDigit()) {
                end++
            }
            return (start + 1 until end).joinToString("") { this[Point(point.x, it)].toString() }
        }


        /**
         * Applies the Game of Life rules to the grid.
         * @param transform the transformation function to apply to each point.
         * @param stepCounter the number of steps to simulate. If not provided, the function will return the grid after it stabilizes.
         * @return the grid after applying the Game of Life rules.
         */
        @JvmName("gameOfLifeBoolean")
        fun Grid<Boolean>.gameOfLife(stepCounter: Int? = null, transform: (Point, Boolean?, Grid<Boolean>) -> Boolean?): Grid<Boolean> {
            var currentGrid = this
            var previousGrid: Grid<Boolean>
            var steps = 0
            do {
                previousGrid = currentGrid.deepCopy()
                val new = currentGrid.deepCopy()
                for (point in new.data.keys) {
                    new[point] = transform(point, currentGrid[point], currentGrid) ?: continue
                }
                currentGrid = new
                steps++
                if (stepCounter != null && steps >= stepCounter) break
            } while (currentGrid != previousGrid)
            return currentGrid
        }

        /**
         * Applies the Game of Life rules to the grid.
         * @param transform the transformation function to apply to each point.
         * @param stepCounter the number of steps to simulate. If not provided, the function will return the grid after it stabilizes.
         * @return the grid after applying the Game of Life rules.
         */
        @JvmName("gameOfLifeChar")
        fun Grid<Char>.gameOfLife(stepCounter: Int? = null, transform: (Point, Char?, Grid<Char>) -> Char?): Grid<Char> {
            var currentGrid = this
            var previousGrid: Grid<Char>
            var steps = 0
            do {
                previousGrid = currentGrid.deepCopy()
                val new = currentGrid.deepCopy()
                for (point in new.data.keys) {
                    new[point] = transform(point, currentGrid[point], currentGrid) ?: continue
                }
                currentGrid = new
                steps++
                if (stepCounter != null && steps >= stepCounter) break
            } while (currentGrid != previousGrid)
            return currentGrid
        }
    }
}