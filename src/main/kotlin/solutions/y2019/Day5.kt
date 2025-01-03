package solutions.y2019

import Solution
import utils.computing.IntCode
import utils.computing.IntCode.Companion.toChannel
import java.io.File

class Day5 : Solution(2019) {
    override fun solvePart1(input: File): String {
        val ic = input.readText().split(",").map { it.toInt() }.toIntArray()
        return IntCode(ic, listOf(1L).toChannel()).run().last().toString()
    }

    override fun solvePart2(input: File): String {
        val ic = input.readText().split(",").map { it.toInt() }.toIntArray()
        return IntCode(ic, listOf(5L).toChannel()).run().last().toString()
    }
}
