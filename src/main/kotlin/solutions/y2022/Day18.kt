package solutions.y2022

import Solution
import utils.grid.Grid3D
import utils.point.Point3D
import java.io.File

class Day18 : Solution(2022) {
    override fun solvePart1(input: File): Any {
        val grid = parse(input)
        return grid.grid.keys.sumOf { grid.getExposedSides(it) }
    }

    override fun solvePart2(input: File): Int {
        val grid = parse(input)

        val x = grid.grid.keys.run { minOf { it.x } - 1..maxOf { it.x } + 1 }
        val y = grid.grid.keys.run { minOf { it.y } - 1..maxOf { it.y } + 1 }
        val z = grid.grid.keys.run { minOf { it.z } - 1..maxOf { it.z } + 1 }

        val queue = mutableListOf(Point3D(x.first, y.first, z.first))
        val visited: MutableSet<Point3D> = mutableSetOf()

        return generateSequence { queue.removeFirstOrNull() }.filter { it !in visited }.sumOf { next ->
            visited += next
            next.getCardinalNeighbors().filter { it.x in x && it.y in y && it.z in z }.count { neighbor ->
                if (grid[neighbor] != true) queue.add(neighbor)
                grid[neighbor] == true
            }
        }
    }

    private fun parse(input: File): Grid3D<Boolean> {
        val grid = Grid3D<Boolean>()
        input.readLines().forEach { grid[Point3D.of(it)] = true }
        return grid
    }
}
