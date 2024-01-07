package solutions.y2020

import Solution
import utils.Utils.rl
import utils.grid.HexGrid
import utils.grid.HexGrid.Companion.gameOfLife
import utils.movement.HexDirection
import utils.point.Point
import java.io.File

class Day24 : Solution(2020) {

    override fun solvePart1(input: File) = init(input).data.count { it.value }

    override fun solvePart2(input: File) = init(input).gameOfLife(100) { point, value, snap ->
        val neighbors = snap.getNeighbors(point).filter { snap[it] == true }.size
        val black = value == true
        when {
            black && (neighbors == 0 || neighbors > 2) -> false
            !black && neighbors == 2 -> true
            else -> black
        }
    }.data.count { it.value }

    private fun init(input: File): HexGrid<Boolean> {
        val grid = HexGrid<Boolean>()
        for (line in input.rl()) {
            var cur = Point(0, 0)
            var nl = line
            while (nl.isNotEmpty()) {
                var dir: String
                if (nl.take(2) in "ne nw se sw") {
                    dir = nl.take(2)
                    nl = nl.drop(2)
                } else {
                    dir = nl.take(1)
                    nl = nl.drop(1)
                }
                cur += HexDirection.of(dir).point
            }
            grid[cur] = grid[cur]?.let { !it } ?: true
        }
        return grid
    }
}