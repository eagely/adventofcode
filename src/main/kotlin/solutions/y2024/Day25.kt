package solutions.y2024

import Solution
import utils.sdnl
import java.io.File

class Day25 : Solution(2024) {

    override fun solvePart1(input: File) = input.sdnl().partition { it[0] == '#' }.let { (keys, locks) -> keys.sumOf { k -> locks.count { l -> l.indices.all { l[it] != k[it] || k[it] != '#' } } } }

    override fun solvePart2(input: File): Any {
        return "me when the chronicle gets delivered"
    }
}