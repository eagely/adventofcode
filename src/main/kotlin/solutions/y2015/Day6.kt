package solutions.y2015

import Solution
import utils.Utils.extractNumbers
import utils.Utils.p
import utils.Utils.rl
import utils.grid.Grid
import java.io.File
import kotlin.math.max

class Day6 : Solution(2015) {
    override fun solvePart1(input: File): Any {
        val grid = Grid<Boolean>()
        val lines = input.rl()
        lines.forEachIndexed { i, it ->
            val s = it.split(",", "through").map { n -> n.extractNumbers().toInt() }
            val rx = s[0]..s[2]
            val ry = s[1]..s[3]
            rx.forEach { x ->
                ry.forEach {y ->
                    grid[x p y] = if(it.contains("toggle")) !(grid[x p y] ?: false) else it.contains("on")
                }
            }
        }
        return grid.getPointsWithValue(true).size
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid<Int>()
        val lines = input.rl()
        lines.forEachIndexed { i, it ->
            val s = it.split(",", "through").map { n -> n.extractNumbers().toInt() }
            val rx = s[0]..s[2]
            val ry = s[1]..s[3]
            rx.forEach { x ->
                ry.forEach {y ->
                    val cur = grid[x p y] ?: 0
                    grid[x p y] = if(it.contains("toggle")) cur + 2 else if (it.contains("on")) cur + 1 else max(0, cur - 1)
                }
            }
        }
        return grid.data.values.sum()
    }
}
