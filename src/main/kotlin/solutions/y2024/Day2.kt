package solutions.y2024

import Solution
import utils.lines
import utils.split
import java.io.File
import kotlin.math.absoluteValue

class Day2 : Solution(2024) {

    override fun solvePart1(input: File) = input.lines.map { it.split().map { it.toInt() } }.count { it.isSafe() }

    override fun solvePart2(input: File): Any {
        return input.lines.map { it.split().map { it.toInt() } }.count { line ->
            for (i in line.indices) {
                if (line.filterIndexed { idx, _ -> idx != i }.isSafe()) return@count true
            }
            return@count false
        }
    }

    private fun List<Int>.isSafe() =
        (this.sorted() == this || this.sortedDescending() == this) && this.zipWithNext()
            .all { (it.first - it.second).absoluteValue in 1..3 }
}