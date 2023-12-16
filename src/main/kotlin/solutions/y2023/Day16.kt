package solutions.y2023

import Solution
import utils.Utils.rl
import utils.grid.Grid
import utils.movement.Direction
import utils.point.Point
import java.io.File
import kotlin.math.max

class Day16 : Solution(2023) {

    override fun solvePart1(input: File) = sim(Grid.of(input.rl()), Point(0, 0), Direction.EAST)

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl())
        var maxval = 0
        for (i in 0 until grid.columns) {
            var start = Point(0, i)
            var res = sim(grid, start, Direction.SOUTH)
            maxval = max(maxval, res)
            start = Point(grid.rows - 1, i)
            res = sim(grid, start, Direction.NORTH)
            maxval = max(maxval, res)
        }
        for (i in 0 until grid.rows) {
            var start = Point(i, 0)
            var res = sim(grid, start, Direction.EAST)
            maxval = max(maxval, res)
            start = Point(i, grid.columns - 1)
            res = sim(grid, start, Direction.WEST)
            maxval = max(maxval, res)
        }
        return maxval
    }

    private fun sim(grid: Grid<Char>, start: Point, dir: Direction): Int {
        val queue = java.util.ArrayDeque<Pair<Point, Direction>>()
        val visited = hashSetOf<Pair<Point, Direction>>()
        processTile(grid, start, dir, queue)
        while (queue.isNotEmpty()) {
            val (p, d) = queue.removeFirst()
            if (p to d in visited) continue
            visited.add(p to d)
            processTile(grid, p.gridPlus(d), d, queue)
        }
        return visited.map { it.first }.distinct().count()
    }

    private fun processTile(grid: Grid<Char>, point: Point, direction: Direction, queue: java.util.ArrayDeque<Pair<Point, Direction>>) {
        when (grid[point]) {
            '\\' -> queue.add(point to direction.toPointOnGrid().invert().toDirectionOnGrid())
            '/' -> queue.add(point to direction.toPointOnGrid().invert().rotate().toDirectionOnGrid())
            '-' -> when (direction) {
                Direction.EAST, Direction.WEST -> queue.add(point to direction)
                Direction.NORTH, Direction.SOUTH -> {
                    queue.add(point to Direction.EAST)
                    queue.add(point to Direction.WEST)
                }
            }
            '|' -> when (direction) {
                Direction.NORTH, Direction.SOUTH -> queue.add(point to direction)
                Direction.EAST, Direction.WEST -> {
                    queue.add(point to Direction.NORTH)
                    queue.add(point to Direction.SOUTH)
                }
            }
            '.' -> queue.add(point to direction)
        }
    }
}
