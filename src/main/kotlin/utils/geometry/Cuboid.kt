package utils.geometry

import utils.grid.Grid
import utils.movement.Direction
import utils.point.Point

@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Cuboid<T>(var rows: Int = 0, var columns: Int = 0) : Collection<T> {
    val data = mutableListOf<Grid<T>>()
    val sides: Int get() = data.size

    fun move(side: Int, point: Point, direction: Direction, wall: Char = '#'): Triple<Int, Point, Direction> {
        val sideData = data[side - 1]
        if (side < 0 || side > sides) throw IllegalArgumentException("Side $side does not exist")
        if (!sideData.contains(point)) throw IllegalArgumentException("Point $point does not exist on side $side")
        if (sideData.rows != sideData.columns) throw IllegalArgumentException("Side $side is not a square")
        return if (data[sides - 1].contains(point.gridPlus(direction)))
            Triple(side, if (sideData[point.gridPlus(direction)] == '#') point else point.gridPlus(direction), direction)
        else
            wrap(side, point, direction)
    }

    fun wrap(side: Int, point: Point, direction: Direction): Triple<Int, Point, Direction> {
        val sideData = data[side - 1]
        return when (side) {
            1, 4 -> when (direction) {
                Direction.NORTH -> Triple((side + 4) % 6 + 1, point + Point(49, 0), Direction.NORTH)
                Direction.EAST -> Triple((side + 2) % 6 + 1, Point(sideData.rows - 1 - point.x, point.y), Direction.WEST)
                Direction.SOUTH -> Triple((side + 1) % 6 + 1, point.invert(), Direction.WEST)
                Direction.WEST -> Triple(side % 6 + 1, point + Point(0, 49), Direction.WEST)
            }

            2, 5 -> when (direction) {
                Direction.NORTH -> Triple((side + 3) % 6 + 1, point.invert(), Direction.EAST)
                Direction.EAST -> Triple((side + 4) % 6 + 1, point - Point(0, 49), Direction.EAST)
                Direction.SOUTH -> Triple(side % 6 + 1, point - Point(49, 0), Direction.SOUTH)
                Direction.WEST -> Triple((side + 2) % 6 + 1, Point(sideData.rows - 1 - point.x, point.y), Direction.EAST)
            }

            3, 6 -> when (direction) {
                Direction.NORTH -> Triple((side + 4) % 6 + 1, point + Point(49, 0), Direction.NORTH)
                Direction.EAST -> Triple(side - 2, point.invert(), Direction.NORTH)
                Direction.SOUTH -> Triple(side % 6 + 1, point - Point(49, 0), Direction.SOUTH)
                Direction.WEST -> Triple((side + 1) % 6 + 1, point.invert(), Direction.SOUTH)
            }
            else -> throw java.lang.IllegalArgumentException("Side $side is not possible in 3D space")
        }
    }

    operator fun set(side: Int, grid: Grid<T>) {
        data[side] = grid
        rows = maxOf(rows, grid.rows)
        columns = maxOf(columns, grid.columns)
    }

    operator fun get(side: Int) = data[side]

    override val size: Int get() = data.size

    override fun isEmpty(): Boolean = data.isEmpty() || data.all { it.isEmpty() }

    override fun iterator(): Iterator<T> {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return elements.all { contains(it) }
    }

    override fun contains(element: T): Boolean {
        return data.any { it.contains(element) }
    }

    override fun toString() = buildString {
        data.forEachIndexed { index, grid ->
            append("Side ${index + 1}:\n")
            append(grid.toString())
            append("\n")
        }
    }

    companion object {
        fun <T> of(vararg grids: Grid<T>): Cuboid<T> {
            val cuboid = Cuboid<T>()
            grids.forEach { cuboid.data.add(it) }
            return cuboid
        }
    }
}
