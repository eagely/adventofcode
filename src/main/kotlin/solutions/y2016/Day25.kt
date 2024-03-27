package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day25 : Solution(2016) {

    override fun solvePart1(input: File): Any {
        val offset = input.lines.let { it[1].extractNumbers().toInt() * it[2].extractNumbers().toInt() }
        var result = 0
        while (result < offset) result = (result shl 2) or 2
        return result - offset
    }

    override fun solvePart2(input: File): Any {
        return "me when i transmit the signal"
    }
}