package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day18 : Solution(2016) {

    override fun solvePart1(input: File) = solve(input).take(40).sumOf { it.count { it } }

    override fun solvePart2(input: File) = solve(input).take(400000).sumOf { it.count { it } }

    private fun solve(input: File) = generateSequence(input.text.map { it == '.' }) { row -> List(row.size) { !(row.getOrElse(it - 1) { true } xor row.getOrElse(it + 1) { true }) } }
}