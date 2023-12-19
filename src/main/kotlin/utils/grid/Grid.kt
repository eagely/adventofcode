package utils.grid

import utils.Utils.abs
import utils.Utils.p
import utils.point.Point
import kotlin.math.sign

@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Grid<T>(val initialRows: Int, val initialColumns: Int) : Collection<T> {
    constructor() : this(0, 0)

    val indices: List<Point> get() = data.keys.toList()

    var data = mutableMapOf<Point, T>()
    val rows: Int get() = maxX - minX + 1
    val columns: Int get() = maxY - minY + 1
    val minX: Int get() = data.keys.minOfOrNull { it.x } ?: 0
    val minY: Int get() = data.keys.minOfOrNull { it.y } ?: 0
    val maxX: Int get() = data.keys.maxOfOrNull { it.x } ?: 0
    val maxY: Int get() = data.keys.maxOfOrNull { it.y } ?: 0

    val min get() = Point(minX, minY)
    val max get() = Point(maxX, maxY)

    operator fun set(points: Set<Point>, value: T) {
        points.forEach { this[it] = value }
    }

    fun add(points: Set<Point>, value: T) {
        points.forEach { point ->
            data.putIfAbsent(point, value)
        }

    }

    operator fun set(row: Int, column: Int, value: T) = set(Point(row, column), value)
    operator fun set(point: Point, value: T) { data[point] = value }

    operator fun set(xRange: IntRange, yRange: IntRange, value: T) = xRange.forEach { x -> yRange.forEach { y -> set(x p y, value) } }
    operator fun set(x: Int, yRange: IntRange, value: T) = yRange.forEach { set(x p it, value) }
    operator fun set(xRange: IntRange, y: Int, value: T) = xRange.forEach { set(it p y, value) }

    operator fun get(row: Int): List<T?> = getRow(row)
    operator fun get(point: Point): T? = data[point]
    operator fun get(row: Int, col: Int): T? = data[Point(row, col)]
    operator fun get(value: T): Point? = data.entries.find { it.value == value }?.key

    fun getOrDefault(point: Point, defaultValue: T): T = data.getOrDefault(point, defaultValue)
    fun getOrDefault(value: T, defaultPoint: Point): Point =
        data.entries.find { it.value == value }?.key ?: defaultPoint

    fun getPointsWithValue(value: T): Set<Point> = data.filterValues { it == value }.keys

    fun replace(oldValue: T, newValue: T): Grid<T> {
        val position = getPointsWithValue(oldValue)
        position.forEach { data[it] = newValue }
        return this
    }

    fun getRow(row: Int) = data.filter { it.key.x == row }.values.toList()
    fun getColumn(col: Int) = data.filter { it.key.y == col }.values.toList()
    fun getRows(): List<List<T?>> = (minX..maxX).map { row -> getRow(row) }
    fun getColumns(): List<List<T?>> = (minY..maxY).map { col -> getColumn(col) }

    fun forEach(action: (T) -> Unit) {
        data.values.forEach(action)
    }

    fun forEachIndexed(action: (Point, T) -> Unit) {
        data.forEach { (point, value) -> action(point, value) }
    }

    fun <R> map(transform: (T) -> R): List<R> {
        return data.values.map(transform)
    }

    fun <R> mapIndexed(transform: (Point, T) -> R): List<R> {
        return data.map { (point, value) -> transform(point, value) }
    }

    fun filter(predicate: (T) -> Boolean) = Grid<T>().apply {
        data = this@Grid.data.filterValues(predicate).toMutableMap()
    }

    fun filterIndexed(predicate: (Point, T) -> Boolean): Grid<T> {
        return Grid<T>().apply { data.filter { (point, value) -> predicate(point, value) }.values.toList() }
    }

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
    fun shiftedBeforeRow(row: Int, dp: Point): Grid<T> = shifted(dp) { point, _ -> point.x < row }
    fun shiftedAfterRow(row: Int, dp: Point): Grid<T> = shifted(dp) { point, _ -> point.x > row }
    fun shiftedBeforeColumn(column: Int, dp: Point): Grid<T> = shifted(dp) { point, _ -> point.y < column }
    fun shiftedAfterColumn(column: Int, dp: Point): Grid<T> = shifted(dp) { point, _ -> point.y > column }
    fun shifted(dp: Point, predicate: (Point, T) -> Boolean): Grid<T> = data.map { if (predicate(it.key, it.value)) it.key + dp to it.value else it.key to it.value }.toMap(mutableMapOf()).let { of(it) }
    fun any(predicate: (T) -> Boolean): Boolean {
        return data.values.any(predicate)
    }

    fun anyIndexed(predicate: (Point, T) -> Boolean): Boolean {
        return data.any { (point, value) -> predicate(point, value) }
    }

    fun all(predicate: (T) -> Boolean): Boolean {
        return data.values.all(predicate)
    }

    fun allIndexed(predicate: (Point, T) -> Boolean): Boolean {
        return data.all { (point, value) -> predicate(point, value) }
    }

    fun none(predicate: (T) -> Boolean): Boolean {
        return data.values.none(predicate)
    }

    fun noneIndexed(predicate: (Point, T) -> Boolean): Boolean {
        return data.none { (point, value) -> predicate(point, value) }
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

    fun getCardinalNeighborPositions(row: Int, column: Int) = Point(row, column).getCardinalNeighbors()

    fun getNeighborPositions(row: Int, col: Int) = Point(row, col).getNeighbors()

    fun getNeighborPositions(point: Point) = point.getNeighbors().filter { it in data.keys }
    fun getNeighbors(point: Point) = getNeighborPositions(point).mapNotNull { get(it) }.toSet()
    fun getCardinalNeighborPositions(point: Point) = point.getCardinalNeighbors().filter { it in data.keys }

    fun getCardinalNeighbors(point: Point) = getCardinalNeighborPositions(point).mapNotNull { get(it) }.toSet()
    fun getNeighbors(row: Int, column: Int): Set<T?> =
        getNeighborPositions(row, column).mapNotNull { get(Point(it.x, it.y)) }.toSet()

    fun getCardinalNeighbors(row: Int, column: Int): Set<T?> =
        getCardinalNeighborPositions(row, column).mapNotNull { get(Point(it.x, it.y)) }.toSet()

    fun movePointsBy(
        points: Set<Point>, xOff: Int, yOff: Int, circular: Boolean = false, skipNull: Boolean = false
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

    fun getFirst(value: T) = (minX..maxX).asSequence().flatMap { row ->
        (minY..maxY).asSequence().map { col -> Point(row, col) }
    }.firstOrNull { point -> data[point] == value }


    fun getLast(value: T) = (maxX downTo minX).asSequence().flatMap { row ->
        (maxY downTo minY).asSequence().map { col -> Point(row, col) }
    }.firstOrNull { point -> data[point] == value }


    fun fillWith(value: T, indexing: Int = 0): Grid<T> {
        for (x in indexing until rows) {
            for (y in indexing until columns) {
                if (data[Point(x, y)] == null) {
                    data[Point(x, y)] = value
                }
            }
        }
        return this
    }

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


    fun addPoints(xRange: IntRange, yRange: IntRange, value: T): Grid<T> = apply {
        xRange.forEach { x -> yRange.forEach { y -> if (data[x p y] == null) set(x p y, value) } }
    }

    @Deprecated("use grid[xRange, yRange] = value instead", ReplaceWith("set(xRange, yRange, value)"))
    fun setPoints(xRange: IntRange, yRange: IntRange, value: T): Grid<T> = apply {
        xRange.forEach { x -> yRange.forEach { y -> set(x p y, value) } }
    }

    fun invert(): Grid<T> {
        val newGrid = Grid<T>(columns, rows)

        for ((point, value) in data) {
            val newPoint = Point(point.y, point.x)
            newGrid[newPoint] = value
        }

        return newGrid
    }


    fun invertRows(): Grid<T> {
        val newGrid = Grid<T>(rows, columns)

        for ((point, value) in data) {
            val newPoint = Point(rows - point.x - 1, point.y)
            newGrid[newPoint] = value
        }

        return newGrid
    }

    fun invertColumns(): Grid<T> {
        val newGrid = Grid<T>(rows, columns)

        for ((point, value) in data) {
            val newPoint = Point(point.x, columns - point.y - 1)
            newGrid[newPoint] = value
        }

        return newGrid
    }

    fun dropExtraRows(other: Grid<T>): Grid<T> {
        val newGrid = Grid<T>(rows, columns)
        for ((point, value) in data) {
            if (other[point] != null) {
                newGrid[point] = value
            }
        }
        return newGrid
    }

    fun dropExtraColumns(other: Grid<T>): Grid<T> {
        val newGrid = Grid<T>(rows, columns)
        for ((point, value) in data) {
            if (other[point] != null) {
                newGrid[point] = value
            }
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

    fun subGrid(minrow: Int, maxrow: Int, mincol: Int, maxcol: Int): Grid<T> {
        val subGrid = Grid<T>(maxrow - minrow + 1, maxcol - mincol + 1)
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

    fun deepCopy(copyFunction: (T) -> T): Grid<T> {
        val newData = mutableMapOf<Point, T>()
        for ((point, value) in this.data) {
            newData[point.copy()] = copyFunction(value)
        }
        return Grid<T>(rows, columns).apply { this.data = newData }
    }

    companion object {
        @JvmName("deepCopyChar")
        fun Grid<Char>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        @JvmName("deepCopyInt")
        fun Grid<Int>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        @JvmName("deepCopyBoolean")
        fun Grid<Boolean>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        @JvmName("ofMap")
        fun <T> of(data: MutableMap<Point, T>): Grid<T> = Grid<T>().apply {
            this.data = data
        }

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

        @JvmName("ofSingleList")
        fun <T : Any> of(rows: List<T>): Grid<T> {
            val data = rows.mapIndexed { index, value ->
                Point(index, 0) to value
            }.toMap().toMutableMap()
            return Grid<T>().apply {
                this.data = data

            }
        }

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

        @JvmName("numberAt")
        fun Grid<Char>.numberAt(point: Point): String? {
            if (get(point) == null || !get(point)!!.isDigit()) return null
            var start = point.y
            while (start >= 0 && get(point.x, start)!!.isDigit()) {
                start--
            }
            var end = point.y
            while (end < columns && get(point.x, end)!!.isDigit()) {
                end++
            }
            return (start + 1 until end).joinToString("") { get(point.x, it).toString() }
        }
    }
}