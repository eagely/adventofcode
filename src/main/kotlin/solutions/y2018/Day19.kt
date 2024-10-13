package solutions.y2018

import Solution
import utils.annotations.NoTest
import utils.lines
import utils.split
import java.io.File

class Day19 : Solution(2018) {

    @NoTest
    override fun solvePart1(input: File) = run(input, 0)

    @NoTest
    override fun solvePart2(input: File) = run(input, 1)

    private fun run(input: File, registerZero: Int): Int {
        val lines = input.lines
        val (a, b) = listOf(lines[22], lines[24]).map { it.split()[2].toInt() }
        val max = 836 + registerZero * 10550400 + a * 22 + b
        return (1..max).filter { max % it == 0 }.sum()
    }
}