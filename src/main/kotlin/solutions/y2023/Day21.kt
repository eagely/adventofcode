package solutions.y2023

import Solution
import utils.Utils.pm
import utils.Utils.rl
import utils.grid.Grid
import utils.point.Point
import java.io.File

class Day21 : Solution(2023) {

    override fun solvePart1(input: File): Any {
        val grid = Grid.of(input.rl())
        if (grid.rows < 50) return "womp womp"
        val queue = ArrayDeque<Pair<Point, Int>>()
        queue.add(grid['S']!! to 0)
        val visited = hashSetOf<Point>()
        while (queue.isNotEmpty()) {
            val (pos, step) = queue.removeFirst()
            if (step == 65) break
            if (pos in visited) continue
            if (step pm 2 == 0) visited.add(pos)
            queue.addAll(pos.getCardinalNeighbors().filter { grid[it] != '#' }.map { it to step + 1 })
        }
        return visited.size
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl())
        if (grid.rows < 50) return "womp womp"
        val req = 26501365L
        var delta = 0L
        var skip = 0L
        val queue = ArrayDeque<Pair<Point, Long>>()
        queue.add(Pair(grid['S']!!, 0))
        val visited = hashSetOf<Point>()
        val size = Point(grid.rows, grid.columns)
        val cycle = size.x * 2
        var lastStep = 0L
        var previousPlots = 0L
        var delta1 = 0L
        var delta2 = 0L
        var plots = 0L
        while (queue.isNotEmpty()) {
            val (position, step) = queue.removeFirst()
            if (position in visited) continue
            if (step % 2 == 1L) visited.add(position)
            if (step % cycle == 66L && step > lastStep) {
                lastStep = step
                if (plots - previousPlots - delta1 == delta2) {
                    delta = plots - previousPlots + delta2
                    skip = step - 1
                    break
                }
                delta2 = (plots - previousPlots) - delta1
                delta1 = plots - previousPlots
                previousPlots = plots
            }
            plots = visited.size.toLong()
            queue.addAll(position.getCardinalNeighbors().filter { grid[it % size] != '#' }.map { it to step + 1 })
        }
        while (skip < req) {
            skip += cycle
            plots += delta
            delta += delta2
        }
        return plots
    }
}