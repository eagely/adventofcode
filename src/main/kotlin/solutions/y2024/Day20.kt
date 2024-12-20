package solutions.y2024

import Solution
import utils.chargrid
import utils.point.Point
import java.io.File

class Day20 : Solution(2024) {

    override fun solvePart1(input: File) = solve(input)

    override fun solvePart2(input: File) = solve(input, 20)

    private fun solve(input: File, cheatDuration: Int = 2): Int {
        val grid = input.chargrid()
        var next = grid['S']!!
        val end = grid['E']!!
        val path = mutableListOf(next)
        val visited = HashSet<Point>()

        while (next != end) {
            visited.add(next)
            next = grid.getCardinalNeighborPositions(next).first { grid[it] != '#' && it !in visited }
            path.add(next)
        }

        return (0..<path.lastIndex - 1).sumOf { i ->
            (i + 1..<path.size).count { j ->
                val dist = path[i].manhattanDistance(path[j])
                dist <= cheatDuration && j - i - dist >= 100
            }
        }
    }
}