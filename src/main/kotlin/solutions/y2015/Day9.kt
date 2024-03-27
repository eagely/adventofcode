package solutions.y2015

import Solution
import java.io.File
import utils.*
import kotlin.math.max
import kotlin.math.min

class Day9 : Solution(2015) {

    override fun solvePart1(input: File) = solve(input).first

    override fun solvePart2(input: File) = solve(input).second

    private fun solve(input: File): Pair<Int, Int> {
        val adjacencies = input.lines.map { it.split(" to ", " = ") }.flatMap { listOf(it[0] to (it[1] to it[2].toInt()), it[1] to (it[0] to it[2].toInt()))}.groupBy({ it.first }, { it.second }).mapValues { it.value.toMap() }
        val locations = (adjacencies.keys + adjacencies.values.map { it.keys }.flatten()).distinct()
        var min = Int.MAX_VALUE
        var max = 0
        for (start in locations) {
            val queue = ArrayDeque(listOf(listOf(start) to 0))
            while (queue.isNotEmpty()) {
                val (cur, steps) = queue.removeFirst()
                if (cur.size == locations.size) {
                    min = min(min, steps)
                    max = max(max, steps)
                }
                if (adjacencies[cur.last()] == null) continue
                queue.addAll(adjacencies[cur.last()]!!.entries.filter { it.key !in cur }.map { (next, distance) -> cur + next to steps + distance })
            }
        }
        return min to max
    }
}