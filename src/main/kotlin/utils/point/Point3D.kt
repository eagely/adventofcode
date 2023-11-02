package utils.point

import kotlin.math.abs

data class Point3D(var x: Int, var y: Int, var z: Int) {
    constructor(x: Number, y: Number, z: Number) : this(x.toInt(), y.toInt(), z.toInt())

    val up: Point3D get() = Point3D(x, y + 1, z)
    val down: Point3D get() = Point3D(x, y - 1, z)
    val left: Point3D get() = Point3D(x - 1, y, z)
    val right: Point3D get() = Point3D(x + 1, y, z)
    val forward: Point3D get() = Point3D(x, y, z + 1)
    val backward: Point3D get() = Point3D(x, y, z - 1)

    fun isInside(maxX: Int, maxY: Int, maxZ: Int): Boolean {
        return x in 0 until maxX && y in 0 until maxY && z in 0 until maxZ
    }

    fun getCardinalNeighbors(): Set<Point3D> {
        return setOf(up, down, left, right, forward, backward)
    }

    fun getNeighbors(): List<Point3D> {
        val neighbors = mutableListOf<Point3D>()
        for (dx in -1..1) {
            for (dy in -1..1) {
                for (dz in -1..1) {
                    if (dx != 0 || dy != 0 || dz != 0) {
                        neighbors.add(Point3D(x + dx, y + dy, z + dz))
                    }
                }
            }
        }
        return neighbors
    }


    fun manhattanDistance(other: Point3D) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Point3D) = Point3D(x - other.x, y - other.y, z - other.z)
    operator fun times(other: Point3D) = Point3D(x * other.x, y * other.y, z * other.z)
    operator fun div(other: Point3D) = Point3D(x / other.x, y / other.y, z / other.z)
    operator fun rem(other: Point3D) = Point3D(x % other.x, y % other.y, z % other.z)

    override fun toString() = "$x-$y-$z"

    companion object {
        fun of(input: String): Point3D {
            val (x, y, z) = input.split(',').map { it.trim().toInt() }
            return Point3D(x, y, z)
        }
    }
}
