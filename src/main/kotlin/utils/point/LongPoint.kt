package utils.point

import utils.movement.Direction
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.sign

data class LongPoint(var x: Long, var y: Long) {
    constructor(x: Number, y: Number) : this(x.toLong(), y.toLong())

    val u: LongPoint get() = LongPoint(x, y + 1)
    val ur: LongPoint get() = LongPoint(x + 1, y + 1)
    val r: LongPoint get() = LongPoint(x + 1, y)
    val dr: LongPoint get() = LongPoint(x + 1, y - 1)
    val d: LongPoint get() = LongPoint(x, y - 1)
    val dl: LongPoint get() = LongPoint(x - 1, y - 1)
    val l: LongPoint get() = LongPoint(x - 1, y)
    val ul: LongPoint get() = LongPoint(x - 1, y + 1)
    val sign: LongPoint get() = LongPoint(x.sign, y.sign)

    fun isInside(maxX: Int, maxY: Int): Boolean {
        return x in 0..<maxX && y in 0..<maxY
    }

    fun getCardinalNeighbors(): Set<LongPoint> {
        return setOf(u, r, d, l)
    }

    fun getNeighbors(): Set<LongPoint> {
        return setOf(u, ur, r, dr, d, dl, l, ul)
    }

    fun lineTo(other: LongPoint): List<LongPoint> {
        val xDelta = (other.x - x).sign
        val yDelta = (other.y - y).sign
        return generateSequence(this) { last ->
            LongPoint(last.x + xDelta, last.y + yDelta).takeIf { it != other }
        }.toList()
    }

    fun rotate(degrees: Int = 180): LongPoint {
        return when (degrees.absoluteValue % 360) {
            0 -> LongPoint(x, y)
            90 -> LongPoint(-y, x)
            180 -> LongPoint(-x, -y)
            270 -> LongPoint(y, -x)
            else -> throw IllegalArgumentException("Rotation must be a multiple of 90 degrees")
        }
    }

    fun invert() = LongPoint(y, x)

    fun getCloserOrEqualPoints(target: LongPoint): Set<LongPoint> =
        (x - this.manhattanDistance(target) .. x + this.manhattanDistance(target)).flatMap { dx ->
            (y - this.manhattanDistance(target) .. y + this.manhattanDistance(target)).mapNotNull { dy ->
                LongPoint(dx, dy).takeIf { it.manhattanDistance(this) <= this.manhattanDistance(target) }
            }
        }.toSet()

    fun mod(value: Int) = LongPoint(this.x % value, this.y % value)

    fun getCloserPoints(target: LongPoint): Set<LongPoint> =
        (x - this.manhattanDistance(target) .. x + this.manhattanDistance(target)).flatMap { dx ->
            (y - this.manhattanDistance(target) .. y + this.manhattanDistance(target)).mapNotNull { dy ->
                LongPoint(dx, dy).takeIf { it.manhattanDistance(this) < this.manhattanDistance(target) }
            }
        }.toSet()

    fun toDirection(): Direction {
        return when {
            x == 0L && y == 1L -> Direction.NORTH
            x == 0L && y == -1L -> Direction.SOUTH
            x == 1L && y == 0L -> Direction.EAST
            x == -1L && y == 0L -> Direction.WEST
            else -> throw IllegalArgumentException("Point $this is not a direction")
        }
    }
    fun toDirectionOnGrid(): Direction {
        return when {
            x == -1L && y == 0L -> Direction.NORTH
            x == 1L && y == 0L -> Direction.SOUTH
            x == 0L && y == 1L -> Direction.EAST
            x == 0L && y == -1L -> Direction.WEST
            else -> throw IllegalArgumentException("Point $this is not a direction")
        }
    }
    fun manhattanDistance(other: LongPoint) = abs(x - other.x) + abs(y - other.y)
    fun gridPlus(other: Direction) = this + other.toLongPoint()
    fun gridMinus(other: Direction) = this - other.toLongPoint()
    operator fun plus(other: LongPoint) = LongPoint(x + other.x, y + other.y)
    operator fun plus(other: Direction) = this + other.toLongPoint()
    operator fun unaryPlus() = LongPoint(+x, +y)
    operator fun minus(other: LongPoint) = LongPoint(x - other.x, y - other.y)
    operator fun minus(other: Direction) = this - other.toLongPoint()
    operator fun unaryMinus() = LongPoint(-x, -y)
    operator fun times(other: LongPoint) = LongPoint(x * other.x, y * other.y)
    operator fun div(other: LongPoint) = LongPoint(x / other.x, y / other.y)
    operator fun rem(other: LongPoint) = LongPoint(x % other.x, y % other.y)
    operator fun inc() = LongPoint(x + 1, y + 1)
    operator fun dec() = LongPoint(x - 1, y - 1)
    operator fun compareTo(other: LongPoint) = (x + y).compareTo(other.x + other.y)
    override fun toString() = "$x-$y"


    companion object {
        val ORIGIN = LongPoint(0, 0)
        fun of(input: String): LongPoint {
            val (x, y) = input.split(',', '-').map { it.trim().toInt() }
            return LongPoint(x, y)
        }
    }
}
