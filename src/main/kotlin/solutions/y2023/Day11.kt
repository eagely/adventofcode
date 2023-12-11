package solutions.y2023

import Solution
import utils.Utils.rl
import utils.Utils.zipWithAllUnique
import utils.grid.Grid
import java.io.File
import kotlin.math.max
import kotlin.math.min

class Day11 : Solution(2023) {

    override fun solvePart1(input: File): Any {
        val grid = Grid.of(input.rl())
        val ecols = grid.getColumns().let { cols -> cols.indices.filter { i -> cols[i].all { it == '.' } } }
        val erows = grid.getRows().let { rows -> rows.indices.filter { i -> rows[i].all { it == '.' } } }
        return grid.getPointsWithValue('#').toList().zipWithAllUnique().sumOf { (start, end) -> start.manhattanDistance(end) + erows.count { it in min(start.x, end.x)..max(start.x, end.x) } + ecols.count { it in min(start.y, end.y)..max(start.y, end.y) }}
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl())
        val ecols = grid.getColumns().let { cols -> cols.indices.filter { i -> cols[i].all { it == '.' } } }
        val erows = grid.getRows().let { rows -> rows.indices.filter { i -> rows[i].all { it == '.' } } }
        return grid.getPointsWithValue('#').toList().zipWithAllUnique().sumOf { (start, end) -> start.manhattanDistance(end).toLong() + erows.count { it in min(start.x, end.x)..max(start.x, end.x) } * 999999 + ecols.count { it in min(start.y, end.y)..max(start.y, end.y) } * 999999 }
    }
}
