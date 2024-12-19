package solutions.y2024

import Solution
import utils.sdnl
import java.io.File

class Day19 : Solution(2024) {

    override fun solvePart1(input: File): Any {
        val (towels, designs) = parse(input)
        return designs.count { dp(it, towels) >= 1 }
    }

    override fun solvePart2(input: File): Any {
        val (towels, designs) = parse(input)
        return designs.sumOf { dp(it, towels) }
    }

    private val cache = HashMap<String, Long>()
    private fun dp(design: String, towels: List<String>): Long {
        if (design.isEmpty()) return 1L
        cache[design]?.let { return it }
        return towels.sumOf { if (design.startsWith(it)) dp (design.drop(it.length), towels) else 0 }.also { cache[design] = it }
    }

    private fun parse(input: File) = input.sdnl().let { it.first().split(", ") to it.drop(1).first().split("\n") }
}