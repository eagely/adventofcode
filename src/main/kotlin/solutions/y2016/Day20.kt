package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day20 : Solution(2016) {

    override fun solvePart1(input: File) = parse(input).first().last + 1

    override fun solvePart2(input: File) = 4294967296 - parse(input).sumOf { it.size }

    private fun parse(input: File) = merge(input.lines.map { it.extractLongsSeparated().let { r -> r.first()..r.last() } }.sortedBy { it.first })
}