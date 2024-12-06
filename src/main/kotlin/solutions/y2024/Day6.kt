package solutions.y2024

import Solution
import utils.contains
import utils.die
import utils.movement.Direction
import utils.point.Point
import java.io.File

class Day6 : Solution(2024) {

    override fun solvePart1(input: File) = input.toCharArrayGrid().getPath().size

    override fun solvePart2(input: File): Any {
        val grid = input.toCharArrayGrid()
        val visited = HashSet<Pair<Point, Direction>>()
        val start = grid.getStart()
        val path = grid.getPath()

        return path.filter { grid[it] == '.' }.count { o ->
            visited.clear()
            var p = start
            var d = Direction.NORTH
            while (true) {
                if (!visited.add(p to d)) return@count true
                if (p !in grid) return@count false
                if (grid[p+d] == '#' || p + d == o) d += Direction.EAST
                else p += d
            }
            false
        }
    }

    private fun Array<CharArray>.getPath(): Set<Point> {
        val visited = HashSet<Pair<Point, Direction>>()
        var p = getStart()
        var d = Direction.NORTH

        while (p to d !in visited && p in this) {
            visited.add(p to d)
            if (get(p + d) == '#') d += Direction.EAST
            else p += d
        }

        return visited.map { it.first }.toSet()
    }

    private fun Array<CharArray>.getStart(): Point {
        for (row in indices) {
            for (col in this[row].indices) {
                if (this[row][col] == '^') return Point(row, col)
            }
        }
        die()
    }

    private operator fun Array<CharArray>.get(point: Point): Char? {
        if (point !in this) return null
        return get(point.x)[point.y]
    }

    private fun File.toCharArrayGrid(): Array<CharArray> {
        return readLines().map { it.toCharArray() }.toTypedArray()
    }
}