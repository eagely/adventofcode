package solutions

import Solution
import utils.Utils.extractNumbers
import utils.Utils.pm
import utils.Utils.rl
import utils.movement.Direction
import utils.point.Point
import java.io.File

class Day12 : Solution(2020) {

    override fun solvePart1(input: File): Any {
        val instr = input.rl().map { it[0] to it.extractNumbers().toInt() }
        var pos = Point.ORIGIN
        var dir = Direction.EAST
        for (i in instr) {
            val (cmd, value) = i
            when (cmd) {
                'F' -> pos += dir.toPoint() * value
                'R' -> dir += Direction.of(value)
                'L' -> dir -= Direction.of(value)
                else -> pos += Direction.of(cmd).toPoint() * value
            }
        }
        return pos.manhattanDistance(Point.ORIGIN)
    }

    override fun solvePart2(input: File): Any {
        val instr = input.rl().map { it[0] to it.extractNumbers().toInt() }
        var pos = Point.ORIGIN
        var wp = Point(10, 1)
        for (i in instr) {
            val (cmd, value) = i
            when (cmd) {
                'F' -> pos += wp * value
                'R' -> wp = wp.rotate(-value)
                'L' -> wp = wp.rotate(value)
                else -> wp -= (Direction.of(cmd) + Direction.WEST).toPoint() * value
            }
        }
        return pos.manhattanDistance(Point.ORIGIN)
    }
}