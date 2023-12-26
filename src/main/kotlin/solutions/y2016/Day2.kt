package solutions.y2016

import Solution
import utils.Utils.rl
import utils.grid.Grid
import utils.movement.Direction
import utils.point.Point
import java.io.File

class Day2 : Solution(2016) {

    override fun solvePart1(input: File): Any {
        val grid = Grid.of(listOf("123", "456", "789"))
        val dirs = input.rl().map { it.map { Direction.of(it) } }
        var cur = Point(1, 1)
        var total = ""
        dirs.forEach {
            it.forEach {
                if (grid[cur.gridPlus(it)] != null)
                cur = cur.gridPlus(it)
            }
            total += grid[cur]
        }
        return total
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(listOf("..1..", ".234.", "56789", ".ABC.", "..D.."))
        val dirs = input.rl().map { it.map { Direction.of(it) } }
        var cur = Point(2, 0)
        var total = ""
        dirs.forEach {
            it.forEach {
                if (grid[cur.gridPlus(it)] != '.' && grid[cur.gridPlus(it)] != null)
                    cur = cur.gridPlus(it)
            }
            total += grid[cur]
        }
        return total
    }
}