package solutions.y2020

import Solution
import utils.Utils.ril
import java.io.File

class Day10 : Solution(2020) {

    override fun solvePart1(input: File) = (input.ril() + 0).sorted().windowed(2).map { it[1] - it[0] }.let { it.count { it == 1 } * (it.count { it == 3 } + 1) }

    override fun solvePart2(input: File): Any {
        val adapters = (input.ril() + 0).sorted()
        val memo = HashMap<Int, Long>()
        fun dp(jolt: Int): Long {
            if (jolt == adapters.last()) return 1
            if (jolt in memo) return memo[jolt]!!
            return adapters.filter { it in jolt + 1..jolt + 3 }.map { dp(it) }.sum().also { memo[jolt] = it }
        }
        return dp(0)
    }
}