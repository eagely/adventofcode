package utils.point

import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sign

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

    fun getCloserOrEqualPoints(target: Point): Set<Point> =
        (x - this.manhattanDistance(target) .. x + this.manhattanDistance(target)).flatMap { dx ->
            (y - this.manhattanDistance(target) .. y + this.manhattanDistance(target)).mapNotNull { dy ->
                Point(dx, dy).takeIf { it.manhattanDistance(this) <= this.manhattanDistance(target) }
            }
        }.toSet()


    fun lineTo(other: Point): List<Point> {
        val xDelta = (other.x - x).sign
        val yDelta = (other.y - y).sign
        val steps = maxOf((x - other.x).absoluteValue, (y - other.y).absoluteValue)
        return generateSequence(this) { last ->
            Point(last.x + xDelta, last.y + yDelta).takeIf { it != other }
        }.toList()
    }



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
