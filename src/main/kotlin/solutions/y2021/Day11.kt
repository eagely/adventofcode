package solutions.y2021

import Solution
import utils.Utils.intgrid
import utils.point.Point
import java.io.File

class Day11 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        var grid = input.intgrid()
        var c = 0
        repeat(100) {
            grid = grid.map { it + 1}
            val flashed = HashSet<Point>()
            while (grid.anyIndexed { p, i -> i > 9 && p !in flashed }) {
                grid.forEachIndexed { p, i ->
                    if (i > 9 && p !in flashed) {
                        flashed.add(p)
                        c++
                        grid[p.getNeighbors()] = { it + 1 }
                    }
                }
            }
            grid[flashed] = 0
        }
        return c
    }

    override fun solvePart2(input: File): Any {
        var grid = input.intgrid()
        var step = 0
        while (true) {
            grid = grid.map { it + 1 }
            val flashed = HashSet<Point>()
            step++
            var c = 0
            while (grid.anyIndexed { p, i -> i > 9 && p !in flashed }) {
                grid.forEachIndexed { p, i ->
                    if (i > 9 && p !in flashed) {
                        flashed.add(p)
                        c++
                        grid[p.getNeighbors()] = { it + 1 }
                    }
                }
            }
            grid[flashed] = 0
            if (c == grid.data.size) break
        }
        return step
    }
}
