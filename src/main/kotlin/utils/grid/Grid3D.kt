package utils.grid

import utils.Utils.p
import utils.point.Point3D

/**
 * A generic dynamic 3d grid implementation.
 * It uses Map<Point3D, T> to store the points, which means accessing a known point is O(1) but indexing a point is O(n).
 * Currently, since Point3D stores Ints, the grid is limited to 2^32 rows and columns.
 * Hence, there is no guarantee that the points will be in any particular order.
 * @param T the type of elements in the grid.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Grid3D<T>(var initialDepth: Int = 0, var initialRows: Int = 0, var initialColumns: Int = 0) : Collection<T> {
    constructor() : this(0, 0)

    /**
     * The indices of the grid
     * @return a list of all the indices in the grid.
     */
    val indices: List<Point3D> get() = data.keys.toList()

    /**
     * The data of the grid, stored as a map of points to values.
     */
    var data = mutableMapOf<Point3D, T>()

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
     * The depth of the grid.
     */
    val depth: Int get() = maxZ - minZ + 1

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
     * The lowest z value in the grid.
     */
    val minZ: Int get() = data.keys.minOfOrNull { it.z } ?: 0

    /**
     * The highest z value in the grid.
     */
    val maxZ: Int get() = data.keys.maxOfOrNull { it.z } ?: 0

    /**
     * Sets all the points in the set to the given value.
     * @param points the points to set.
     * @param value the value to set the points to.
     */
    operator fun set(points: Set<Point3D>, value: T) {
        points.forEach { this[it] = value }
    }

    /**
     * Adds all the points in the set if they are not present in the grid and sets them to the given value.
     * @param points the points to add.
     * @param value the value to set the points to.
     */
    fun add(points: Set<Point3D>, value: T) {
        points.forEach { point -> data.putIfAbsent(point, value) }
    }

    /**
     * Sets the point at the specified Point to the given value.
     * @param point the point to set.
     * @param value the value to set the point to.
     */
    operator fun set(point: Point3D, value: T) {
        data[point] = value
    }

    /**
     * Sets the points in the specified Cuboid to the given value.
     * @param xRange the range of x values to set.
     * @param yRange the range of y values to set.
     * @param zRange the range of z values to set.
     * @param value the value to set the points to.
     */
    operator fun set(xRange: IntRange, yRange: IntRange, zRange: IntRange, value: T) = xRange.forEach { x -> yRange.forEach { y -> zRange.forEach { z -> set(x p y p z, value) } } }

    /**
     * Returns the value at the specified point.
     * @param point the point to get the value of.
     * @return the value at the specified point, or null if absent.
     */
    operator fun get(point: Point3D): T? = data[point]

    /**
     * Returns the first point with the specified value.
     * @param value the value to search for.
     * @return the first point with the specified value, or null if absent.
     */
    operator fun get(value: T) = data.entries.find { it.value == value }?.key


    /**
     * Returns the value at the specified Point3D, or defaultValue if absent.
     * @param point the point to get the value of.
     * @param defaultValue the value to return if the point is absent.
     * @return the value at the specified point, or defaultValue if absent.
     */
    fun getOrDefault(point: Point3D, defaultValue: T) = data.getOrDefault(point, defaultValue)

    /**
     * Returns the first point with the specified value, or defaultPoint if absent.
     * @param value the value to search for.
     * @param defaultPoint the point to return if the value is absent.
     * @return the first point with the specified value, or defaultPoint if absent.
     */
    fun getOrDefault(value: T, defaultPoint: Point3D) = data.entries.find { it.value == value }?.key ?: defaultPoint

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
     * Returns the specified layer.
     * @param lay the layer to get.
     * @return the specified layer.
     */
    fun getLayer(lay: Int) = data.filter { it.key.z == lay }.values.toList()

    /**
     * Returns a list of all rows.
     * @return a list of all rows.
     */
    fun getRows() = (minX..maxX).map { row -> getRow(row) }

    /**
     * Returns a list of all columns.
     * @return a list of all columns.
     */
    fun getColumns() = (minY..maxY).map { col -> getColumn(col) }

    /**
     * Returns a list of all layers.
     * @return a list of all layers.
     */
    fun getLayers() = (minZ..maxZ).map { lay -> getLayer(lay) }

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
    fun forEachIndexed(action: (Point3D, T) -> Unit) {
        data.forEach { (point, value) -> action(point, value) }
    }

    /**
     * Maps all points in the grid to a specified transform.
     * @param transform the transform to apply to each point.
     * @return a new shallow copy grid containing the mapped points.
     */
    fun <R> map(transform: (T) -> R) = Grid3D<R>().apply { data = this@Grid3D.data.mapValues { transform(it.value) }.toMutableMap() }

    /**
     * Maps all points in the grid to a specified transform with their points as indices.
     * @param transform the transform to apply to each point.
     * @return a new shallow copy grid containing the mapped points.
     */
    fun <R> mapIndexed(transform: (Point3D, T) -> R) = Grid3D<R>().apply { data = this@Grid3D.data.mapValues { (p, v) -> transform(p, v) }.toMutableMap() }

    /**
     * Filters all points in the grid to those that match the specified predicate.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy grid containing the filtered points.
     */
    fun filter(predicate: (T) -> Boolean) = Grid3D<T>().apply { data = this@Grid3D.data.filterValues(predicate).toMutableMap() }

    /**
     * Filters all points in the grid to those that match the specified predicate with their points as indices.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy grid containing the filtered points.
     */
    fun filterIndexed(predicate: (Point3D, T) -> Boolean) = Grid3D<T>().apply { data.filter { (point, value) -> predicate(point, value) }.values.toList() }

    /**
     * Filters all points in the grid to those that match the specified predicate, but keeps consecutively matching points together.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy Grid3D<String> containing the filtered points, with each string being a consecutive sequence of matching points.
     */
    fun filterConsecutive(lay: Int, predicate: (T) -> Boolean): Grid3D<String> {
        val result = data.entries.fold(mutableListOf<Pair<Point3D, StringBuilder>>()) { acc, (point, value) ->
            if (predicate(value)) {
                if (acc.isEmpty() || point != acc.last().first + Point3D(0, 1, lay)) {
                    acc.add(point to StringBuilder(value.toString()))
                } else {
                    acc.last().second.append(value)
                }
            }
            acc
        }
        val resultGrid = Grid3D<String>()
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
    fun anyIndexed(predicate: (Point3D, T) -> Boolean): Boolean {
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
    fun allIndexed(predicate: (Point3D, T) -> Boolean): Boolean {
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
    fun noneIndexed(predicate: (Point3D, T) -> Boolean): Boolean {
        return data.none { (point, value) -> predicate(point, value) }
    }

    /**
     * Returns a Set of all points that are diagonal or cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a Set of all points that are diagonal or cardinal neighbors of the specified point and are in the grid.
     */
    fun getNeighborPositions(point: Point3D) = point.getNeighbors().filter { it in data.keys }

    /**
     * Returns a List of all values that are diagonal or cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a List of all values that are diagonal or cardinal neighbors of the specified point and are in the grid.
     */
    fun getNeighbors(point: Point3D) = getNeighborPositions(point).map { this[it] }

    /**
     * Returns a Set of all points that are cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a Set of all points that are cardinal neighbors of the specified point and are in the grid.
     */
    fun getCardinalNeighborPositions(point: Point3D) = point.getCardinalNeighbors().filter { it in data.keys }

    /**
     * Returns a List of all values that are cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a List of all values that are cardinal neighbors of the specified point and are in the grid.
     */
    fun getCardinalNeighbors(point: Point3D) = getCardinalNeighborPositions(point).mapNotNull { this[it] }

    /**
     * Replaces all null points in the grid with the given value, turning the grid into a rectangle.
     * This method is non local and will affect the grid.
     * @param value the value to replace null points with.
     * @return the grid after replacing all null points with the given value.
     */
    fun fillWith(value: T): Grid3D<T> {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    if (data[Point3D(x, y, z)] == null) {
                        data[Point3D(x, y, z)] = value
                    }
                }
            }
        }
        return this
    }

    /**
     * Flood fills all points cardinally (6-way) connected to startPoint with the specified newValue.
     * This method is non local and will affect the grid.
     * @param startPoint the point to start the flood fill from.
     * @param newValue the value to set all points to.
     * @return the grid after flood filling.
     */
    fun floodFill(startPoint: Point3D, newValue: T): Grid3D<T> {
        val originalValue = get(startPoint) ?: return this
        val visited = mutableSetOf<Point3D>()
        val queue = ArrayDeque<Point3D>()
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
     * Returns true if the endPoint is cardinally (6-way) connected to startPoint by a flood fill.
     * @param startPoint the point to start the flood fill from.
     * @param endPoint the point to check if it is connected to startPoint.
     * @return true if the endPoint is connected to startPoint by a flood fill.
     */
    fun floodFillContains(startPoint: Point3D, endPoint: Point3D): Boolean {
        val match = get(startPoint) ?: return false
        val visited = mutableSetOf<Point3D>()
        val queue = ArrayDeque<Point3D>()
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
    fun canMove(points: Set<Point3D>, dz: Int, dx: Int, dy: Int, vararg free: Point3D?) = points.all { point ->
        val np = point + Point3D(dz, dx, dy)
        if (free.isEmpty()) this[np] == null else np in free || np in points
    }

    /**
     * Returns the number of exposed sides (not connected to any other points in the grid) of the specified point.
     * @param point the point to get the number of exposed sides of.
     * @return the number of exposed sides of the specified point.
     */
    fun getExposedSides(point: Point3D) = 6 - point.getCardinalNeighbors().filter { it in data.keys }.size
    
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
    fun contains(element: Point3D) = data.containsKey(element)

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

        other as Grid3D<*>

        if (depth != other.depth || rows != other.rows || columns != other.columns) return false

        for (z in 0..<depth) {
            for (x in 0..<rows) {
                for (y in 0..<columns) {
                    if (this[Point3D(x, y, z)] != other[Point3D(x, y, z)]) return false
                }
            }
        }

        return true
    }

    /**
     * Returns a hash code value for the grid.
     * @return a hash code value for the grid.
     */
    override fun hashCode(): Int {
        var result = depth
        result = 31 * result + rows
        result = 31 * result + columns
        result = 31 * result + data.hashCode()
        return result
    }

    /**
     * Returns a deep copy of the grid.
     * @return a deep copy of the grid.
     */
    fun deepCopy(copyFunction: (T) -> T): Grid3D<T> {
        val newData = mutableMapOf<Point3D, T>()
        for ((point, value) in this.data) {
            newData[point.copy()] = copyFunction(value)
        }
        return Grid3D<T>(rows, columns).apply { this.data = newData }
    }

    /**
     * Returns a String representation of the grid.
     * @return a String representation of the grid.
     */
    override fun toString(): String {
        val builder = StringBuilder()
        for (z in 0 until depth) {
            builder.append("z=$z\n")
            for (x in 0 until rows) {
                for (y in 0 until columns) {
                    val value = this[Point3D(x, y, z)]
                    builder.append(value ?: ".")
                }
                builder.append("\n")
            }
            builder.append("\n")
        }
        return builder.toString()
    }

    /**
     * The following methods are deprecated and are only kept because they are used in some of my solutions.
     * They may be removed at any point, do not use them.
     */

    /**
     * Sets the point at the specified z, x, y to the given value.
     * @param z the z coordinate of the point.
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * @param value the value to set the point to.
     */
    @Deprecated("Use point setter instead", ReplaceWith("this[Point3D(x, y, z)] = value"))
    operator fun set(z: Int, x: Int, y: Int, value: T) { data[Point3D(z, x, y)] = value }

    /**
     * Gets the point at the specified z, x, y.
     * @param z the z coordinate of the point.
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * @return the value at the specified point, or null if absent.
     */
    @Deprecated("Use point getter instead", ReplaceWith("this[Point3D(x, y, z)]"))
    operator fun get(z: Int, x: Int, y: Int): T? = data[Point3D(z, x, y)]

    companion object {
        /**
         * Deep copies the Grid3D<Char>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyChar")
        fun Grid3D<Char>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Int>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyInt")
        fun Grid3D<Int>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Boolean>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyBoolean")
        fun Grid3D<Boolean>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<String>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyString")
        fun Grid3D<String>.deepCopy() = of(this.data.map { it.key.copy() to String(it.value.toCharArray()) }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Double>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyDouble")
        fun Grid3D<Double>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Float>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyFloat")
        fun Grid3D<Float>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Long>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyLong")
        fun Grid3D<Long>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Short>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyShort")
        fun Grid3D<Short>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Byte>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyByte")
        fun Grid3D<Byte>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))
        
        /**
         * Create a new grid from the specified mutable map data.
         * @param data the data to create the grid from.
         * @return a new grid from the specified mutable map data.
         */
        @JvmName("ofMutableMap")
        fun <T> of(data: MutableMap<Point3D, T>): Grid3D<T> = Grid3D<T>().apply {
            this.data = data
        }

        /**
         * Create a new grid from the specified map data.
         * @param data the data to create the grid from.
         * @return a new grid from the specified map data.
         */
        @JvmName("ofMap")
        fun <T> of(data: Map<Point3D, T>): Grid3D<T> = Grid3D<T>().apply {
            this.data = data.toMutableMap()
        }

        @JvmName("ofTripleList")
        fun <T> of(listOfRows: List<List<List<T>>>): Grid3D<T> {
            val depth = listOfRows.size
            var rows = 0
            var columns = 0
            val data = mutableMapOf<Point3D, T>()

            listOfRows.forEachIndexed { z, layer ->
                layer.forEachIndexed { x, row ->
                    row.forEachIndexed { y, element ->
                        data[Point3D(x, y, z)] = element
                        rows = maxOf(rows, x + 1)
                        columns = maxOf(columns, y + 1)
                    }
                }
            }

            return Grid3D<T>(depth, rows, columns).apply {
                this.data = data
            }
        }

        @JvmName("ofDoubleStringList")
        fun of(listOfRows: List<List<String>>): Grid3D<Char> {
            val depth = listOfRows.size
            var rows = 0
            var columns = 0
            val data = mutableMapOf<Point3D, Char>()

            listOfRows.forEachIndexed { z, layer ->
                layer.forEachIndexed { x, row ->
                    row.forEachIndexed { y, element ->
                        data[Point3D(x, y, z)] = element
                        rows = maxOf(rows, x + 1)
                        columns = maxOf(columns, y + 1)
                    }
                }
            }

            return Grid3D<Char>(depth, rows, columns).apply {
                this.data = data
            }
        }

        @JvmName("gameOfLifeBoolean")
        fun Grid3D<Boolean>.gameOfLife(stepCounter: Int? = null, transform: (Point3D, Boolean?, Grid3D<Boolean>) -> Boolean): Grid3D<Boolean> {
            var currentGrid = this
            var previousGrid: Grid3D<Boolean>
            var steps = 0
            do {
                previousGrid = currentGrid.deepCopy()
                val new = currentGrid.deepCopy()
                new.data.keys.forEach { point -> new[point] = transform(point, currentGrid[point], currentGrid) }
                currentGrid = new
                steps++
                if (stepCounter != null && steps >= stepCounter) break
            } while (currentGrid != previousGrid)
            return currentGrid
        }

        @JvmName("gameOfLifeChar")
        fun Grid3D<Char>.gameOfLife(stepCounter: Int? = null, transform: (Point3D, Char?, Grid3D<Char>) -> Char): Grid3D<Char> {
            var currentGrid = this
            var steps = 0

            while (stepCounter == null || steps < stepCounter) {
                val newGrid = currentGrid.deepCopy()
                val pointsToConsider = currentGrid.data.keys.flatMap { point ->
                    point.getNeighbors()
                }.toSet()

                for (point in pointsToConsider) {
                    val currentValue = currentGrid[point]
                    newGrid[point] = transform(point, currentValue, currentGrid)
                }

                if (newGrid == currentGrid) break

                currentGrid = newGrid
                steps++
            }

            return currentGrid
        }

    }
}