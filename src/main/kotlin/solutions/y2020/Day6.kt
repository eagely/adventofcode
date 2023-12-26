package solutions

import Solution
import utils.Utils.distinct
import utils.Utils.sdnl
import java.io.File

class Day6 : Solution(2020) {
    override fun solvePart1(input: File): Any {
        return input.sdnl().sumOf { it.replace("\n", "").distinct().count() }
    }

    override fun solvePart2(input: File): Any {
        return input.sdnl().sumOf { it.lines().reduce { a, b -> a.toSet().intersect(b.toSet()).joinToString("") }.length }
    }
}
