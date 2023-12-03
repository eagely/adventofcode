package solutions.y2023

import Solution
import utils.Utils.product
import utils.Utils.rl
import utils.grid.Grid
import utils.grid.Grid.Companion.numberAt
import java.io.File

class Day3 : Solution(2023) {
    override fun solvePart1(input: File): Int {
        val grid = Grid.of(input.rl())
        return grid.filterConsecutive { it.toString().matches("[^0-9.]".toRegex()) }.mapIndexed { point, _ -> point.getNeighbors().mapNotNull { grid.numberAt(it) }.distinct() }.flatten().sumOf { it.toInt() }
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl())
        return grid.filter { it == '*' }.mapIndexed { point, _ -> point.getNeighbors().mapNotNull { grid.numberAt(it)?.toIntOrNull() }.distinct().let { if (it.size == 2) it.product() else 0 } }.sum()
    }
}