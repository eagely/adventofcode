package solutions.y2021

import Solution
import utils.Utils.asInt
import utils.Utils.product
import utils.Utils.rl
import utils.grid.Grid
import java.io.File

class Day9 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val grid = Grid.of(input.rl())
        return grid.filterIndexed { p, c -> grid.getCardinalNeighbors(p).none { it.asInt() <= c.asInt() } }.sumOf { it.asInt() + 1 }
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl()).filter { it != '9' }
        return grid.separate().map { it.size }.sortedDescending().take(3).product()
    }
}