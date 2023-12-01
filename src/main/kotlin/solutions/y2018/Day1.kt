package solutions.y2018

import Solution
import utils.Utils.rl
import java.io.File

class Day1 : Solution(2018) {
    override fun solvePart1(input: File): Any {
        var output = 0
        val lines = input.rl().map { it.toInt() }
        lines.forEach { output += it }
        return output
    }

    override fun solvePart2(input: File): Any {
        var output = 0
        val prev = HashSet<Int>()
        val lines = input.rl().map { it.toInt() }
        while (true) {
            lines.forEach {
                output += it
                if (!prev.add(output)) return output
            }
        }
    }
}
