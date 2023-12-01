package solutions.y2017

import Solution
import utils.Utils.asInt
import utils.Utils.at
import utils.Utils.rt
import utils.Utils.s
import java.io.File

class Day1 : Solution(2017) {
    override fun solvePart1(input: File): Any {
        val nums = (input.rt() + input.rt().first()).map { it.asInt() }.zipWithNext()
        return nums.filter { it.first == it.second }.sumOf { it.first }
    }

    override fun solvePart2(input: File): Any {
        val nums = input.rt().map { it.asInt() }
        return nums.filterIndexed { i, it -> it == (nums at i + nums.s / 2) }.sum()
    }
}
