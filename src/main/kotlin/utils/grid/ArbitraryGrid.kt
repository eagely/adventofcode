package utils.grid

import utils.point.ArbitraryPoint

/**
 * A generic dynamic arbitrary dimension grid implementation.
 * It uses Map<ArbitraryPoint, T> to store the points, which means accessing a known point is O(1) but indexing a point is O(n).
 * Hence, there is no guarantee that the points will be in any particular order.
 * Currently, since ArbitraryPoint stores Ints, the grid is limited to 2^32 rows and columns.
 * @param T the type of elements in the grid.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
data class ArbitraryGrid<T>(var data: MutableMap<ArbitraryPoint, T>) : Collection<T> {
    constructor() : this(mutableMapOf())
    /**
     * The indices of the grid
     * @return a list of all the indices in the grid.
     */
    val indices: List<ArbitraryPoint> get() = data.keys.toList()

    /**
     * The amount of dimensions in the grid.
     */
    val dimensions: Int get() = data.keys.maxOfOrNull { it.c.size } ?: 0

    /**
     * The number of elements in the grid.
     */
    override val size: Int get() = data.size
    
    fun size(dimension: Int) = max(dimension) - min(dimension) + 1
    fun min(dimension: Int) = data.keys.minBy { it.c[dimension] }.c[dimension]
    fun max(dimension: Int) = data.keys.maxBy { it.c[dimension] }.c[dimension]

    /**
     * Sets all the points in the set to the given value.
     * @param points the points to set.
     * @param value the value to set the points to.
     */
    operator fun set(points: Set<ArbitraryPoint>, value: T) {
        points.forEach { this[it] = value }
    }

    /**
     * Adds all the points in the set if they are not present in the grid and sets them to the given value.
     * @param points the points to add.
     * @param value the value to set the points to.
     */
    fun add(points: Set<ArbitraryPoint>, value: T) {
        points.forEach { point -> data.putIfAbsent(point, value) }
    }

    /**
     * Sets the point at the specified Point to the given value.
     * @param point the point to set.
     * @param value the value to set the point to.
     */
    operator fun set(point: ArbitraryPoint, value: T) {
        data[point] = value
    }

    /**
     * Returns the value at the specified point.
     * @param point the point to get the value of.
     * @return the value at the specified point, or null if absent.
     */
    operator fun get(point: ArbitraryPoint): T? = data[point]

    /**
     * Returns the first point with the specified value.
     * @param value the value to search for.
     * @return the first point with the specified value, or null if absent.
     */
    operator fun get(value: T) = data.entries.find { it.value == value }?.key


    /**
     * Returns the value at the specified ArbitraryPoint, or defaultValue if absent.
     * @param point the point to get the value of.
     * @param defaultValue the value to return if the point is absent.
     * @return the value at the specified point, or defaultValue if absent.
     */
    fun getOrDefault(point: ArbitraryPoint, defaultValue: T) = data.getOrDefault(point, defaultValue)

    /**
     * Returns the first point with the specified value, or defaultPoint if absent.
     * @param value the value to search for.
     * @param defaultPoint the point to return if the value is absent.
     * @return the first point with the specified value, or defaultPoint if absent.
     */
    fun getOrDefault(value: T, defaultPoint: ArbitraryPoint) = data.entries.find { it.value == value }?.key ?: defaultPoint

    /**
     * Returns a Set of all points with the specified value.
     * @param value the value to search for.
     * @return a Set of all points with the specified value.
     */
    fun getPointsWithValue(value: T) = data.filterValues { it == value }.keys
 
    fun getLine(pos: Int, dimension: Int) = data.keys.filter { it.c[dimension] == pos }.toSet()
    fun getLines(dimension: Int) = (min(dimension)..max(dimension)).map { getLine(it, dimension) }.toSet()
    
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
    fun forEachIndexed(action: (ArbitraryPoint, T) -> Unit) {
        data.forEach { (point, value) -> action(point, value) }
    }

    /**
     * Maps all points in the grid to a specified transform.
     * @param transform the transform to apply to each point.
     * @return a new shallow copy grid containing the mapped points.
     */
    fun <R> map(transform: (T) -> R) = ArbitraryGrid<R>().apply { data = this@ArbitraryGrid.data.mapValues { transform(it.value) }.toMutableMap() }

    /**
     * Maps all points in the grid to a specified transform with their points as indices.
     * @param transform the transform to apply to each point.
     * @return a new shallow copy grid containing the mapped points.
     */
    fun <R> mapIndexed(transform: (ArbitraryPoint, T) -> R) = ArbitraryGrid<R>().apply { data = this@ArbitraryGrid.data.mapValues { (p, v) -> transform(p, v) }.toMutableMap() }

    /**
     * Filters all points in the grid to those that match the specified predicate.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy grid containing the filtered points.
     */
    fun filter(predicate: (T) -> Boolean) = ArbitraryGrid<T>().apply { data = this@ArbitraryGrid.data.filterValues(predicate).toMutableMap() }

    /**
     * Filters all points in the grid to those that match the specified predicate with their points as indices.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy grid containing the filtered points.
     */
    fun filterIndexed(predicate: (ArbitraryPoint, T) -> Boolean) = ArbitraryGrid<T>().apply { data.filter { (point, value) -> predicate(point, value) }.values.toList() }

    /**
     * Filters all points in the grid to those that match the specified predicate, but keeps consecutively matching points together.
     * @param predicate the predicate to filter by.
     * @return a new shallow copy Grid3D<String> containing the filtered points, with each string being a consecutive sequence of matching points.
     */
    fun filterConsecutive(lay: Int, predicate: (T) -> Boolean): ArbitraryGrid<String> {
        val result = data.entries.fold(mutableListOf<Pair<ArbitraryPoint, StringBuilder>>()) { acc, (point, value) ->
            if (predicate(value)) {
                if (acc.isEmpty() || point != acc.last().first + ArbitraryPoint(0, 1, lay)) {
                    acc.add(point to StringBuilder(value.toString()))
                } else {
                    acc.last().second.append(value)
                }
            }
            acc
        }
        val resultGrid = ArbitraryGrid<String>()
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
    fun anyIndexed(predicate: (ArbitraryPoint, T) -> Boolean): Boolean {
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
    fun allIndexed(predicate: (ArbitraryPoint, T) -> Boolean): Boolean {
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
    fun noneIndexed(predicate: (ArbitraryPoint, T) -> Boolean): Boolean {
        return data.none { (point, value) -> predicate(point, value) }
    }

    /**
     * Returns a Set of all points that are diagonal or cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a Set of all points that are diagonal or cardinal neighbors of the specified point and are in the grid.
     */
    fun getNeighborPositions(point: ArbitraryPoint) = point.getNeighbors().filter { it in data.keys }

    /**
     * Returns a List of all values that are diagonal or cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a List of all values that are diagonal or cardinal neighbors of the specified point and are in the grid.
     */
    fun getNeighbors(point: ArbitraryPoint) = getNeighborPositions(point).map { this[it] }

    /**
     * Returns a Set of all points that are cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a Set of all points that are cardinal neighbors of the specified point and are in the grid.
     */
    fun getCardinalNeighborPositions(point: ArbitraryPoint) = point.getCardinalNeighbors().filter { it in data.keys }

    /**
     * Returns a List of all values that are cardinal neighbors of the specified point and are in the grid.
     * @param point the point to get the neighbors of.
     * @return a List of all values that are cardinal neighbors of the specified point and are in the grid.
     */
    fun getCardinalNeighbors(point: ArbitraryPoint) = getCardinalNeighborPositions(point).mapNotNull { this[it] }

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
    fun contains(element: ArbitraryPoint) = data.containsKey(element)

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

        other as ArbitraryGrid<*>

        if (size != other.size) return false

        return data.keys.all { this[it] == other[it] }
    }

    /**
     * Returns a hash code value for the grid.
     * @return a hash code value for the grid.
     */
    override fun hashCode(): Int {
        var result = data.hashCode()
        result = 31 * result + indices.hashCode()
        result = 31 * result + dimensions
        result = 31 * result + size
        return result
    }

    /**
     * Returns a deep copy of the grid.
     * @return a deep copy of the grid.
     */
    fun deepCopy(copyFunction: (T) -> T): ArbitraryGrid<T> {
        val newData = mutableMapOf<ArbitraryPoint, T>()
        for ((point, value) in this.data) {
            newData[point.copy()] = copyFunction(value)
        }
        return ArbitraryGrid<T>().apply { this.data = newData }
    }

    /**
     * Returns a String representation of the grid.
     * @return a String representation of the grid.
     */
    override fun toString(): String {
        return "Arbitrary Grid with $dimensions dimensions and $size elements, point data is $data"
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
    @Deprecated("Use point setter instead", ReplaceWith("this[ArbitraryPoint(x, y, z)] = value"))
    operator fun set(z: Int, x: Int, y: Int, value: T) {
        data[ArbitraryPoint(z, x, y)] = value
    }

    /**
     * Gets the point at the specified z, x, y.
     * @param z the z coordinate of the point.
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * @return the value at the specified point, or null if absent.
     */
    @Deprecated("Use point getter instead", ReplaceWith("this[ArbitraryPoint(x, y, z)]"))
    operator fun get(z: Int, x: Int, y: Int): T? = data[ArbitraryPoint(z, x, y)]



    companion object {
        /**
         * Deep copies the Grid3D<Char>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyChar")
        fun ArbitraryGrid<Char>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Int>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyInt")
        fun ArbitraryGrid<Int>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Boolean>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyBoolean")
        fun ArbitraryGrid<Boolean>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<String>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyString")
        fun ArbitraryGrid<String>.deepCopy() = of(this.data.map { it.key.copy() to String(it.value.toCharArray()) }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Double>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyDouble")
        fun ArbitraryGrid<Double>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Float>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyFloat")
        fun ArbitraryGrid<Float>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Long>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyLong")
        fun ArbitraryGrid<Long>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Short>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyShort")
        fun ArbitraryGrid<Short>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Deep copies the Grid3D<Byte>
         * @return a deep copy of the grid.
         */
        @JvmName("deepCopyByte")
        fun ArbitraryGrid<Byte>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        /**
         * Create a new grid from the specified mutable map data.
         * @param data the data to create the grid from.
         * @return a new grid from the specified mutable map data.
         */
        @JvmName("ofMutableMap")
        fun <T> of(data: MutableMap<ArbitraryPoint, T>): ArbitraryGrid<T> = ArbitraryGrid<T>().apply {
            this.data = data
        }

        /**
         * Create a new grid from the specified map data.
         * @param data the data to create the grid from.
         * @return a new grid from the specified map data.
         */
        @JvmName("ofMap")
        fun <T> of(data: Map<ArbitraryPoint, T>): ArbitraryGrid<T> = ArbitraryGrid<T>().apply {
            this.data = data.toMutableMap()
        }

        @JvmName("ofStringList")
        fun of(list: List<String>, dimensions: Int): ArbitraryGrid<Char> {
            val data = mutableMapOf<ArbitraryPoint, Char>()

            list.forEachIndexed { x, row ->
                row.forEachIndexed { y, element ->
                    data[ArbitraryPoint(listOf(x, y) + List(dimensions-2) { 0 })] = element
                }
            }

            return ArbitraryGrid<Char>().apply {
                this.data = data
            }
        }

        @JvmName("gameOfLifeChar")
        fun ArbitraryGrid<Char>.gameOfLife(stepCounter: Int? = null, transform: (ArbitraryPoint, Char?, ArbitraryGrid<Char>) -> Char?): ArbitraryGrid<Char> {
            var currentGrid = this
            var steps = 0

            while (stepCounter == null || steps < stepCounter) {
                val newGrid = currentGrid.deepCopy()
                val pointsToConsider = currentGrid.data.keys.flatMap { point ->
                    point.getNeighbors()
                }.toSet()

                for (point in pointsToConsider) {
                    transform(point, currentGrid[point], currentGrid)?.let { newGrid[point] = it }
                }

                if (newGrid == currentGrid) break

                currentGrid = newGrid
                steps++
            }

            return currentGrid
        }

    }
}