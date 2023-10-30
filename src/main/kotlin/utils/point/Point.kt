package utils.point

import kotlin.math.abs

data class Point(var x: Int, var y: Int) {
    constructor(x: Number, y: Number) : this(x.toInt(), y.toInt())

    val up: Point get() = Point(x, y + 1)
    val upRight: Point get() = Point(x + 1, y + 1)
    val right: Point get() = Point(x + 1, y)
    val downRight: Point get() = Point(x + 1, y - 1)
    val down: Point get() = Point(x, y - 1)
    val downLeft: Point get() = Point(x - 1, y - 1)
    val left: Point get() = Point(x - 1, y)
    val upLeft: Point get() = Point(x - 1, y + 1)

    fun isInside(maxX: Int, maxY: Int): Boolean {
        return x in 0..<maxX && y in 0..<maxY
    }

    fun getCardinalNeighbors(): List<Point> {
        return listOf(up, right, down, left)
    }

    fun getNeighbors(): List<Point> {
        return listOf(up, upRight, right, downRight, down, downLeft, left, upLeft)
    }

    fun manhattanDistance(other: Point) = abs(x - other.x) + abs(y - other.y)

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun unaryPlus() = Point(+x, +y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun unaryMinus() = Point(-x, -y)
    operator fun times(other: Point) = Point(x * other.x, y * other.y)
    operator fun div(other: Point) = Point(x / other.x, y / other.y)
    operator fun rem(other: Point) = Point(x % other.x, y % other.y)
    operator fun inc() = Point(x + 1, y + 1)
    operator fun dec() = Point(x - 1, y - 1)
    operator fun compareTo(other: Point) = (x + y).compareTo(other.x + other.y)
    override fun toString() = "$x-$y"

    companion object {
        fun of(input: String): Point {
            val (x, y) = input.split(',').map { it.trim().toInt() }
            return Point(x, y)
        }
    }
}
