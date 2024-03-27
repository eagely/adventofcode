package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day23 : Solution(2016) {

    override fun solvePart1(input: File) = 5040 + parse(input)

    override fun solvePart2(input: File) = 479001600 + parse(input)

    private fun parse(input: File) = input.lines.let { it[19].extractNumbers().toInt() * it[20].extractNumbers().toInt() }
}