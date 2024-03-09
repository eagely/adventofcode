package utils.point

import utils.Utils.pm
import kotlin.math.*

data class Point3D(var x: Int, var y: Int, var z: Int) {
    constructor(x: Number, y: Number, z: Number) : this(x.toInt(), y.toInt(), z.toInt())

    val up: Point3D get() = Point3D(x, y, z + 1)
    val down: Point3D get() = Point3D(x, y, z - 1)
    val left: Point3D get() = Point3D(x - 1, y, z)
    val right: Point3D get() = Point3D(x + 1, y, z)
    val forward: Point3D get() = Point3D(x, y + 1, z)
    val backward: Point3D get() = Point3D(x, y - 1, z)

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

    infix fun manhattanDistance(other: Point3D) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    infix fun distance(other: Point3D) = sqrt(abs((x - other.x)).toDouble().pow(2) + abs((y - other.y)).toDouble().pow(2) + abs((z - other.z)).toDouble().pow(2))

    operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)
    operator fun plus(other: Int) = Point3D(x + other, y + other, z + other)
    operator fun minus(other: Point3D) = Point3D(x - other.x, y - other.y, z - other.z)
    operator fun minus(other: Int) = Point3D(x - other, y - other, z - other)
    operator fun times(other: Point3D) = Point3D(x * other.x, y * other.y, z * other.z)
    operator fun times(other: Int) = Point3D(x * other, y * other, z * other)
    operator fun div(other: Int) = Point3D(x / other, y / other, z / other)
    operator fun div(other: Point3D) = Point3D(x / other.x, y / other.y, z / other.z)
    operator fun rem(other: Point3D) = Point3D(x pm other.x, y pm other.y, z pm other.z)
    operator fun rem(other: Int) = Point3D(x pm other, y pm other, z pm other)

    override fun toString() = "$x-$y-$z"

    companion object {
        val ORIGIN = Point3D(0, 0, 0)
        fun of(input: String): Point3D {
            val (x, y, z) = input.split(',', ' ').map { it.trim().toInt() }
            return Point3D(x, y, z)
        }
    }
}
