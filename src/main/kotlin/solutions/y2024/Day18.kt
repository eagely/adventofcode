package solutions.y2024

import Solution
import utils.annotations.NoTest
import utils.lines
import utils.point.Point
import java.io.File

class Day18 : Solution(2024) {

    private var offset = 1024

    @NoTest
    override fun solvePart1(input: File) = solve(input.lines.take(offset).map { Point.of(it) }.toSet())!!

    @NoTest
    override fun solvePart2(input: File): Any {
        val lines = input.lines
        val points = lines.map { Point.of(it) }

        var max = lines.size
        var min = offset
        while (min < max) {
            val mid = (max + min) / 2
            if (solve(points.take(mid).toSet()) == null) max = mid else min = mid + 1
        }
        return points[min - 1].let { "${it.x},${it.y}" }
    }

    private fun solve(points: Set<Point>): Int? {
        val maxX = 70
        val maxY = 70
        val end = Point(maxX, maxY)
        val queue = ArrayDeque<Pair<Point, Int>>()
        queue.add(Point(0, 0) to 0)
        val seen = HashSet<Point>()

        while (queue.isNotEmpty()) {
            val (cur, cost) = queue.removeFirst()

            if (!seen.add(cur)) continue
            if (cur == end) return cost

            queue.addAll(
                cur.getCardinalNeighbors()
                    .filter { it.x in 0..maxX && it.y in 0..maxY }
                    .filter { it !in points }
                    .map { it to cost + 1 }
            )
        }

        return null
    }
}