package solutions.y2024

import Solution
import utils.intgrid
import utils.point.Point
import java.io.File

class Day10 : Solution(2024) {

    override fun solvePart1(input: File): Any {
        val grid = input.intgrid()
        var ans = 0
        for (p in grid.data.keys.filter { grid[it] == 0 }) {
            val visited = HashSet<Point>()
            val queue = ArrayDeque<Point>()
            queue.add(p)

            while (queue.isNotEmpty()) {
                val cur = queue.removeFirst()
                if (!visited.add(cur)) continue
                queue.addAll(grid.getCardinalNeighborPositions(cur).filter { grid[it]!! == grid[cur]!! + 1 })
            }

            ans += visited.count { grid[it] == 9 }
        }


        return ans
    }

    override fun solvePart2(input: File): Any {
        val grid = input.intgrid()
        var ans = 0
        for (p in grid.data.keys.filter { grid[it] == 0 }) {
            val visited = HashSet<List<Point>>()
            val queue = ArrayDeque<List<Point>>()
            queue.add(listOf(p))

            while (queue.isNotEmpty()) {
                val cur = queue.removeFirst()
                if (!visited.add(cur)) continue
                queue.addAll(grid.getCardinalNeighborPositions(cur.last()).filter { grid[it]!! == grid[cur.last()]!! + 1 }.map { cur + it })
            }

            ans += visited.count { grid[it.last()] == 9 }
        }

        return ans
    }
}