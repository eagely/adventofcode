package solutions.y2017

import Solution
import utils.extractNumbersSeparated
import java.io.File

class Day13 : Solution(2017) {

    override fun solvePart1(input: File) = parse(input).entries.fold(0) { acc, (d, r) -> if (d % (r * 2 - 2) == 0) acc + d * r else acc }

    override fun solvePart2(input: File): Int = parse(input).let { depths -> generateSequence(0) { it + 1 }.first { depths.none { (d, r) -> (d + it) % ((r - 1) * 2) == 0 } } }

    private fun parse(input: File) = input.readLines().map { it.extractNumbersSeparated() }.associate { it.first() to it.last() }
}