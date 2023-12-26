package solutions.y2020

import Solution
import utils.Utils.product
import utils.Utils.rl
import utils.grid.Grid
import utils.point.Point
import java.io.File

class Day3 : Solution(2020) {

    override fun solvePart1(input: File): Any {
        val grid = Grid.of(input.rl())
        var trees = 0
        for (acc in 0 until grid.rows) if (grid[Point(acc, acc * 3 % grid.columns)] == '#') trees++
        return trees
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl())
        val trees = MutableList(5) { 0L }
        val slopes = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
        for (acc in 0 until grid.rows) for ((i, p) in slopes.withIndex()) if (grid[Point(acc * p.second, acc * p.first % grid.columns)] == '#') trees[i]++
        return trees.product()
    }
}
