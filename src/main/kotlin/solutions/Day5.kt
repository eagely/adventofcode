package solutions

import Solution
import utils.IntCode
import java.io.File

class Day5 : Solution(2019) {
    override fun solvePart1(input: File): String {
        val ic = input.readText().split(",").map { it.toInt() }.toIntArray()
        return IntCode(ic, 1).run().last().toString()
    }

    override fun solvePart2(input: File): String {
        val ic = input.readText().split(",").map { it.toInt() }.toIntArray()
        return IntCode(ic, 5).run().last().toString()
    }
}
