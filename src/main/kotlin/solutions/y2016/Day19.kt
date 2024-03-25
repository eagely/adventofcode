package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day19 : Solution(2016) {

    override fun solvePart1(input: File): Any {
        val n = input.text.toInt()
        return (n * 2).takeHighestOneBit().inv() and ((n shl 1) or 1)
    }

    override fun solvePart2(input: File): Any {
        val n = input.text.toInt()
        val p = 3 pow ilog(n, 3)
        if (n == p) return 1
        if (n <= 2 * p) return n - p
        return 2 * n - 3 * p
    }
}