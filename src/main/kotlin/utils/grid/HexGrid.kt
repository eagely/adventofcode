package utils.grid

import utils.Utils.println
import utils.movement.HexDirection
import utils.point.Point
import kotlin.math.abs

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * I will clean this up later
 */
data class HexGrid<T>(var data: MutableMap<Point, T>) {
    constructor() : this(mutableMapOf())

    operator fun get(point: Point) = data[point]

    operator fun set(point: Point, value: T) {
        data[point] = value
    }

    fun <Y> map(transform: (T) -> Y) = HexGrid(data.map { it.key to transform(it.value) }.toMap(mutableMapOf()))

    fun getNeighbors(point: Point) = listOf(HexDirection.NW.point, HexDirection.NE.point, HexDirection.E.point, HexDirection.SE.point, HexDirection.SW.point, HexDirection.W.point).map { point + it }.filter { it in data.keys }

    override fun toString(): String {
        if (data.isEmpty()) return "Empty HexGrid"

        val minX = data.keys.minOfOrNull { it.x } ?: 0
        val maxX = data.keys.maxOfOrNull { it.x } ?: 0
        val minY = data.keys.minOfOrNull { it.y } ?: 0
        val maxY = data.keys.maxOfOrNull { it.y } ?: 0

        val sb = StringBuilder()

        for (r in minX..maxX) {
            val offset = -r
            sb.append(" ".repeat(abs(offset)))

            for (c in minY..maxY) {
                val point = Point(r, c)
                if (point in data) {
                    sb.append("${data[point]} ")
                } else {
                    sb.append("+ ")
                }
            }
            sb.append("\n")
        }

        return sb.toString()
    }

    companion object {
        fun print(grid: HexGrid<Boolean>) {
            grid.map { if (it) 'B' else 'W' }.toString().also { it.println() }
        }
        fun HexGrid<Boolean>.deepCopy() = of(this.data.map { it.key.copy() to it.value }.toMap(mutableMapOf()))

        @JvmName("ofMutableMap")
        fun <T> of(data: MutableMap<Point, T>): HexGrid<T> = HexGrid<T>().apply {
            this.data = data
        }

        fun <T> of(data: Map<Point, T>): HexGrid<T> = HexGrid<T>().apply {
            this.data = data.toMutableMap()
        }

        fun HexGrid<Boolean>.gameOfLife(length: Int? = null, transform: (Point, Boolean?, HexGrid<Boolean>) -> Boolean?): HexGrid<Boolean> {
            var cur = this
            var steps = 0

            while (length == null || steps < length) {
                val new = cur.deepCopy()
                val wanted = HashSet<Point>(cur.data.keys) + cur.data.keys.map { HexDirection.getNeighbors(it) }.flatten()
                wanted.forEach {
                    val nv = transform(it, cur[it], cur)
                    if (nv != null) new[it] = nv
                }
                if (new == cur) break
                cur = new
                steps++
            }
            return cur
        }
    }
}