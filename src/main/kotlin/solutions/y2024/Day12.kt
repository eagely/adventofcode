package solutions.y2024

import Solution
import utils.chargrid
import utils.grid.Grid
import utils.point.Point
import java.io.File

class Day12 : Solution(2024) {

    override fun solvePart1(input: File) = solve(input) { grid, cur, c -> cur.getCardinalNeighbors().filter { grid[it] != c }.size }

    override fun solvePart2(input: File) = solve(input) { grid, cur, _ -> grid.isHowManyCorners(cur) }

    private fun solve(input: File, getPerimeter: (Grid<Char>, Point, Char) -> Int): Int {
        val grid = input.chargrid()
        val seen = mutableSetOf<Point>()

        return grid.data.keys.sumOf { p ->
            val connected = HashSet<Point>()
            val c = grid[p]!!
            val q = ArrayDeque<Point>()
            q.add(p)
            var perimeter = 0
            while (q.isNotEmpty()) {
                val cur = q.removeFirst()
                if (!seen.add(cur)) continue
                connected.add(cur)
                perimeter += getPerimeter(grid, cur, c)
                q.addAll(grid.getCardinalNeighborPositions(cur).filter { grid[it]!! == c })
            }
            perimeter * connected.size
        }
    }
}