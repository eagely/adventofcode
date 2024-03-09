package solutions.y2017

import Solution
import arrow.core.some
import java.io.File
import utils.*
import utils.grid.Grid
import utils.point.Point

class Day19 : Solution(2017) {

    override fun solvePart1(input: File) = input.walk().first

    override fun solvePart2(input: File) = input.walk().second

    private fun File.walk(): Pair<String, Int> {
        val grid = Grid.of(this.lines)
        var pos = Point(0, grid.getPointsWithValue('|').minBy { it.x }.y)
        var dir = Point(1, 0)
        var letters = ""
        var steps = 0
        while (true) {
            steps++
            pos += dir
            val c = grid[pos] ?: break
            if (c.isLetter()) letters += c
            if (c == '+') dir = grid.getCardinalNeighborPositions(pos).first { it != pos - dir } - pos
        }
        return letters to steps
    }
}