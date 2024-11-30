package solutions.y2018

import Solution
import utils.lines
import utils.point.ArbitraryPoint
import java.io.File

class Day25 : Solution(2018) {

    override fun solvePart1(input: File): Any {
        val lines = input.lines.map { ArbitraryPoint(it.split(',').map { it.toInt() }) }.toList()
        val seen = HashSet<ArbitraryPoint>()

        var c = 0

        while (lines.any { it !in seen }) {
            val q = ArrayDeque<ArbitraryPoint>()
            q.add(lines.first { it !in seen })

            while (q.isNotEmpty()) {
                val cur = q.removeFirst()
                if (!seen.add(cur)) continue

                q.addAll(
                    lines.filter { it !in seen && it.manhattanDistance(cur) <= 3 }
                )
            }
            c++
        }
        return c
    }

    override fun solvePart2(input: File): Any {
        return "yet another sleepless december coming up"
    }
}