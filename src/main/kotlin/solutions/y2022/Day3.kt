package solutions.y2022

import Solution
import utils.Utils.halve
import utils.Utils.rl
import java.io.File

class Day3 : Solution(2022) {
    override fun solvePart1(input: File): Any {
        return input.rl()
            .map { it.halve().first to it.halve().second }
            .map { it.first.toSet().intersect(it.second.toSet()) }
            .sumOf { if (it.first().isUpperCase()) it.first().code - 38 else it.first().code - 96 }
    }

    override fun solvePart2(input: File): Any {
        return input.rl().chunked(3)
            .map { it[0].toSet().intersect(it[1].toSet()).intersect(it[2].toSet()) }
            .sumOf { if (it.first().isUpperCase()) it.first().code - 38 else it.first().code - 96 }
    }
}
