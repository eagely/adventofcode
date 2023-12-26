package solutions.y2015

import Solution
import utils.Utils.rt
import java.io.File

class Day1 : Solution(2015) {

    override fun solvePart1(input: File) = input.rt().let { it.count { it == '(' } - it.count { it == ')' } }

    override fun solvePart2(input: File): Int {
        var floor = 0
        input.rt().forEachIndexed { i, it ->
            floor += if (it == '(') 1 else -1
            if (floor == -1) return i + 1
        }
        return -1
    }
}
