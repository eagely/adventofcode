package solutions.y2018

import Solution
import utils.chargrid
import utils.annotations.NoTest
import utils.grid.Grid
import utils.grid.Grid.Companion.gameOfLife
import utils.point.Point
import java.io.File

class Day18 : Solution(2018) {

    override fun solvePart1(input: File): Any {
        val grid = input.chargrid().gameOfLife(10, rules)
        return grid.count { it == '|' } * grid.count { it == '#' }
    }

    @NoTest
    override fun solvePart2(input: File): Any {
        var grid = input.chargrid()
        var count = 0
        val seen = HashMap<Int, Int>()
        var cycleStart = 0
        var cycleLength = 0
        var repeats = 0
        while (true) {
            grid = grid.gameOfLife(1, rules)
            count++
            val resources = grid.count { it == '|' } * grid.count { it == '#' }

            if (resources in seen) {
                val old = seen[resources]!!
                if (cycleLength == count - old) {
                    if (cycleStart == 0)
                        cycleStart = count - 1
                    if (++repeats > cycleLength) {
                        return seen.entries.first { (_, v) -> v == (1_000_000_000 - cycleStart) % cycleLength + cycleStart }.key
                    }
                }
                else {
                    cycleLength = count - old
                }
            }
            seen[resources] = count
        }
    }

    private val rules = { p: Point, c: Char?, g: Grid<Char> ->
        val neighbors = g.getNeighbors(p)
        when (c) {
            '.' -> {
                if (neighbors.count { it == '|' } >= 3) '|'
                else '.'
            }

            '|' -> {
                if (neighbors.count { it == '#' } >= 3) '#'
                else '|'
            }

            '#' -> {
                if ('#' in neighbors && '|' in neighbors) '#'
                else '.'
            }

            else -> c
        }
    }
}