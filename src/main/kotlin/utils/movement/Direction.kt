package utils.movement

import utils.point.Point

enum class Direction(val angle: Int) {
    NORTH(0), EAST(90), SOUTH(180), WEST(270);

    operator fun plus(other: Direction) = Direction.of((angle + other.angle) % 360)
    operator fun minus(other: Direction) = Direction.of((angle - other.angle) % 360)
    fun extendOnGrid() = when (this) {
        NORTH -> listOf(Point(-1, -1), Point(-1, 0), Point(-1, 1))
        EAST -> listOf(Point(-1, 1), Point(0, 1), Point(1, 1))
        SOUTH -> listOf(Point(1, -1), Point(1, 0), Point(1, 1))
        WEST -> listOf(Point(-1, -1), Point(0, -1), Point(1, -1))
    }

    fun extend() = when (this) {
        NORTH -> listOf(Point(-1, 1), Point(0, 1), Point(1, 1))
        EAST -> listOf(Point(1, 1), Point(1, 0), Point(1, -1))
        SOUTH -> listOf(Point(-1, -1), Point(0, -1), Point(1, -1))
        WEST -> listOf(Point(-1, 1), Point(-1, 0), Point(-1, -1))
    }

    fun toPointOnGrid() = when (this) {
        NORTH -> Point(-1, 0)
        EAST -> Point(0, 1)
        SOUTH -> Point(1, 0)
        WEST -> Point(0, -1)
    }

    fun toPoint() = when (this) {
        NORTH -> Point(1, 0)
        EAST -> Point(0, 1)
        SOUTH -> Point(-1, 0)
        WEST -> Point(0, -1)
    }

    companion object {
        fun of(c: Char): Direction {
            return when (c.uppercaseChar()) {
                'N', 'U' -> NORTH
                'E', 'R' -> EAST
                'S', 'D' -> SOUTH
                'W', 'L' -> WEST
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