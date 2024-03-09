package utils.point

import utils.Utils.pm
import kotlin.math.*

data class LongPoint3D(var x: Long, var y: Long, var z: Long) {
    constructor(x: Number, y: Number, z: Number) : this(x.toLong(), y.toLong(), z.toLong())

    val up: LongPoint3D get() = LongPoint3D(x, y, z + 1)
    val down: LongPoint3D get() = LongPoint3D(x, y, z - 1)
    val left: LongPoint3D get() = LongPoint3D(x - 1, y, z)
    val right: LongPoint3D get() = LongPoint3D(x + 1, y, z)
    val forward: LongPoint3D get() = LongPoint3D(x, y + 1, z)
    val backward: LongPoint3D get() = LongPoint3D(x, y - 1, z)

    fun isInside(maxX: Long, maxY: Long, maxZ: Long): Boolean {
        return x in 0 until maxX && y in 0 until maxY && z in 0 until maxZ
    }

    fun getCardinalNeighbors(): Set<LongPoint3D> {
        return setOf(up, down, left, right, forward, backward)
    }

    fun getNeighbors(): List<LongPoint3D> {
        val neighbors = mutableListOf<LongPoint3D>()
        for (dx in -1..1) {
            for (dy in -1..1) {
                for (dz in -1..1) {
                    if (dx != 0 || dy != 0 || dz != 0) {
                        neighbors.add(LongPoint3D(x + dx, y + dy, z + dz))
                    }
                }
            }
        }
        return neighbors
    }

    infix fun manhattanDistance(other: LongPoint3D) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    infix fun distance(other: LongPoint3D) = sqrt(abs((x - other.x)).toDouble().pow(2) + abs((y - other.y)).toDouble().pow(2) + abs((z - other.z)).toDouble().pow(2))

    operator fun plus(other: LongPoint3D) = LongPoint3D(x + other.x, y + other.y, z + other.z)
    operator fun plus(other: Long) = LongPoint3D(x + other, y + other, z + other)
    operator fun minus(other: LongPoint3D) = LongPoint3D(x - other.x, y - other.y, z - other.z)
    operator fun minus(other: Long) = LongPoint3D(x - other, y - other, z - other)
    operator fun times(other: LongPoint3D) = LongPoint3D(x * other.x, y * other.y, z * other.z)
    operator fun times(other: Long) = LongPoint3D(x * other, y * other, z * other)
    operator fun div(other: Long) = LongPoint3D(x / other, y / other, z / other)
    operator fun div(other: LongPoint3D) = LongPoint3D(x / other.x, y / other.y, z / other.z)
    operator fun rem(other: LongPoint3D) = LongPoint3D(x pm other.x, y pm other.y, z pm other.z)
    operator fun rem(other: Long) = LongPoint3D(x pm other, y pm other, z pm other)

    override fun toString() = "$x-$y-$z"

    companion object {
        val ORIGIN = LongPoint3D(0, 0, 0)
        fun of(input: String): LongPoint3D {
            val (x, y, z) = input.split(',', ' ').map { it.trim().toLong() }
            return LongPoint3D(x, y, z)
        }
    }
}
