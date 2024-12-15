package solutions.y2024

import Solution
import utils.grid.Grid
import utils.movement.Direction
import utils.point.Point
import utils.remove
import utils.text
import java.io.File
import java.util.*

class Day15 : Solution(2024) {

    private fun parse(input: File, double: Boolean = false) = input.text.split("\n\n").let { (a, b) ->
        Grid.of(a.let {
            if (double) it.replace("#", "##").replace("O", "[]").replace(".", "..").replace("@", "@.")
            else it
        }.split("\n")) to b.remove("\n").map { Direction.of(it) }
    }

    override fun solvePart1(input: File) = parse(input).let { (grid, route) -> solve(grid, route) }

    override fun solvePart2(input: File) = parse(input, true).let { (grid, route) -> solve(grid, route) }

    private fun solve(grid: Grid<Char>, route: List<Direction>): Int {
        var cur = grid['@']!!
        grid[cur] = '.'
        route@ for (i in route) {
            when (grid[cur + i]!!) {
                '.' -> cur += i
                '#' -> continue
                '[', ']' -> {
                    val seen = HashSet<Point>()
                    val s = Stack<Point>()
                    s.add(cur + i)

                    while (s.isNotEmpty()) {
                        val p = s.removeLast()
                        if (!seen.add(p)) continue

                        when (grid[p]) {
                            '[' -> s.add(p + Direction.EAST)
                            ']' -> s.add(p + Direction.WEST)
                        }

                        when (grid[p + i]) {
                            '[', ']' -> s.add(p + i)
                            '#' -> continue@route
                        }
                    }

                    seen.associateWith { grid[it]!! }
                        .onEach { (p, _) -> grid[p] = '.' }
                        .forEach { (p, c) -> grid[p + i] = c }
                    cur += i
                }

                'O' -> {
                    var box = cur + i
                    while (grid[box + i] == 'O') {
                        box += i
                    }
                    if (grid[box + i] == '#') continue
                    grid[box + i] = 'O'
                    grid[cur + i] = '.'
                    cur += i
                }
            }
        }

        return grid.data.entries.sumOf { (p, c) -> if (c !in "[O") 0 else (p.x) * 100 + (p.y) }
    }
}