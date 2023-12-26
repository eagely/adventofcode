package solutions.y2020

import Solution
import utils.Utils.rl
import java.io.File

class Day5 : Solution(2020) {

    override fun solvePart1(input: File) = input.rl().maxOf { calculateSeatId(it) }

    override fun solvePart2(input: File) = input.rl().map { calculateSeatId(it) }.sorted().zipWithNext().first { (a, b) -> b - a == 2 }.first + 1

    private fun calculateSeatId(line: String): Int {
        var rowRange = 0..127
        var colRange = 0..7
        line.forEachIndexed { index, char ->
            when {
                index < 7 -> rowRange = halveRange(rowRange, char == 'F')
                else -> colRange = halveRange(colRange, char == 'L')
            }
        }
        return rowRange.first * 8 + colRange.first
    }

    private fun halveRange(range: IntRange, lowerHalf: Boolean) = if (lowerHalf) range.first..(range.first + range.last) / 2 else (range.first + range.last) / 2 + 1..range.last
}
