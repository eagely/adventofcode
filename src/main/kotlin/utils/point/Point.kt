package utils.point

import kotlin.math.*

data class Point(var x: Int, var y: Int) {
    constructor(x: Number, y: Number) : this(x.toInt(), y.toInt())

    val u: Point get() = Point(x, y + 1)
    val ur: Point get() = Point(x + 1, y + 1)
    val r: Point get() = Point(x + 1, y)
    val dr: Point get() = Point(x + 1, y - 1)
    val d: Point get() = Point(x, y - 1)
    val dl: Point get() = Point(x - 1, y - 1)
    val l: Point get() = Point(x - 1, y)
    val ul: Point get() = Point(x - 1, y + 1)

    fun isInside(maxX: Int, maxY: Int): Boolean {
        return x in 0..<maxX && y in 0..<maxY
    }

    fun getCardinalNeighbors(): List<Point> {
        return listOf(u, r, d, l)
    }

    fun getNeighbors(): Set<Point> {
        return setOf(u, ur, r, dr, d, dl, l, ul)
    }

    fun lineTo(other: Point): List<Point> {
        val xDelta = (other.x - x).sign
        val yDelta = (other.y - y).sign
        return generateSequence(this) { last ->
            Point(last.x + xDelta, last.y + yDelta).takeIf { it != other }
        }.toList()
    }

    fun rotate(degrees: Int = 180): Point {
        return when (degrees.absoluteValue % 360) {
            0 -> Point(x, y)
            90 -> Point(-y, x)
            180 -> Point(-x, -y)
            270 -> Point(y, -x)
            else -> throw IllegalArgumentException("Rotation must be a multiple of 90 degrees")
        }
    }

    fun getCloserOrEqualPoints(target: Point): Set<Point> =
        (x - this.manhattanDistance(target) .. x + this.manhattanDistance(target)).flatMap { dx ->
            (y - this.manhattanDistance(target) .. y + this.manhattanDistance(target)).mapNotNull { dy ->
                Point(dx, dy).takeIf { it.manhattanDistance(this) <= this.manhattanDistance(target) }
            }
        }.toSet()

    fun getCloserPoints(target: Point): Set<Point> =
        (x - this.manhattanDistance(target) .. x + this.manhattanDistance(target)).flatMap { dx ->
            (y - this.manhattanDistance(target) .. y + this.manhattanDistance(target)).mapNotNull { dy ->
                Point(dx, dy).takeIf { it.manhattanDistance(this) < this.manhattanDistance(target) }
            }
        }.toSet()

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
