package utils.movement

import utils.point.LongPoint
import utils.point.Point

enum class Direction(val angle: Int) {
    NORTH(0), EAST(90), SOUTH(180), WEST(270);
    fun opposite() = Direction.of(angle + 180)
    operator fun plus(other: Direction) = Direction.of((angle + other.angle) % 360)
    operator fun minus(other: Direction) = Direction.of((angle - other.angle) % 360)
    operator fun component1() = this.toPoint().x
    operator fun component2() = this.toPoint().y

    @Deprecated("Use extend() instead", ReplaceWith("extend()"))
    fun extendOnGrid() = when (this) {
        NORTH -> listOf(Point(-1, -1), Point(-1, 0), Point(-1, 1))
        EAST -> listOf(Point(-1, 1), Point(0, 1), Point(1, 1))
        SOUTH -> listOf(Point(1, -1), Point(1, 0), Point(1, 1))
        WEST -> listOf(Point(-1, -1), Point(0, -1), Point(1, -1))
    }

    fun extend() = when (this) {
        NORTH -> listOf(Point(-1, -1), Point(-1, 0), Point(-1, 1))
        EAST -> listOf(Point(-1, 1), Point(0, 1), Point(1, 1))
        SOUTH -> listOf(Point(1, -1), Point(1, 0), Point(1, 1))
        WEST -> listOf(Point(-1, -1), Point(0, -1), Point(1, -1))
    }

    @Deprecated("Use toPoint() instead", ReplaceWith("toPoint()"))
    fun toPointOnGrid() = when (this) {
        NORTH -> Point(-1, 0)
        EAST -> Point(0, 1)
        SOUTH -> Point(1, 0)
        WEST -> Point(0, -1)
    }

    fun toLongPoint() = when (this) {
        NORTH -> LongPoint(-1L, 0L)
        EAST -> LongPoint(0L, 1L)
        SOUTH -> LongPoint(1L, 0L)
        WEST -> LongPoint(0L, -1L)
    }

    fun toPoint() = when (this) {
        NORTH -> Point(-1, 0)
        EAST -> Point(0, 1)
        SOUTH -> Point(1, 0)
        WEST -> Point(0, -1)
    }

    companion object {
        fun of(c: Char): Direction {
            return when (c.uppercaseChar()) {
                'N', 'U', '^', '3' -> NORTH
                'E', 'R', '>', '0' -> EAST
                'S', 'D', 'V', '1' -> SOUTH
                'W', 'L', '<', '2' -> WEST
                else -> throw IllegalArgumentException("Invalid rotation: $c")
            }
        }

        fun of(i: Int): Direction {
            return when ((i + 720) % 360) {
                0 -> NORTH
                90 -> EAST
                180 -> SOUTH
                270 -> WEST
                else -> throw IllegalArgumentException("Invalid rotation: $i")
            }
        }
    }
}