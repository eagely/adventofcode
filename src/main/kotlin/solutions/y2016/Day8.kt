package solutions.y2016

import Solution
import java.io.File
import utils.*
import utils.grid.Grid
import utils.point.Point

class Day8 : Solution(2016) {

    override fun solvePart1(input: File) = getGrid(input).count { it }

    override fun solvePart2(input: File) = getGrid(input).map { if (it) '#' else ' ' }

    private fun getGrid(input: File): Grid<Boolean> {
        val grid = Grid<Boolean>()
        val bounds = Point(6, 50)
        input.lines.forEach { line ->
            val nums = line.extractNumbersSeparated()
            when {
                line.startsWith("rect") -> grid[0..<nums[1], 0..<nums[0]] = true
                line.contains("column") -> grid.shiftColumn(nums[0]) { p, _ -> Point(p.x + nums[1], p.y) % bounds}
                line.contains("row") -> grid.shiftRow(nums[0]) { p, _ -> Point(p.x, p.y + nums[1]) % bounds}
            }
        }
        return grid
    }
}