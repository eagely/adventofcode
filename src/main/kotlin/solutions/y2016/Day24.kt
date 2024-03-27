package solutions.y2016

import Solution
import java.io.File
import utils.*
import utils.annotations.NoTest
import utils.movement.PathFinding

class Day24 : Solution(2016) {

    private lateinit var adjacencies: List<List<Int>>

    override fun solvePart1(input: File): Any {
        val grid = input.lines.grid()
        adjacencies = (0..grid.data.values.filter { it.isDigit() }.maxOf { it.asInt() }).map { i -> (0..7).map { j -> PathFinding.bfsCost(grid[i.asChar()], grid[j.asChar()]) { grid.getCardinalNeighborPositions(it!!).filter { grid[it] != '#' } } } }
        return heldKarp().first
    }

    @NoTest
    override fun solvePart2(input: File) = heldKarp().second

    private fun heldKarp(): Pair<Int, Int> {
        val n = adjacencies.size
        val dp = Array(1 shl n) { Array<Int?>(n) { null } }
        dp[1][0] = 0

        for (mask in 1 until (1 shl n)) {
            for (i in 0 until n) {
                if (mask and (1 shl i) != 0) {
                    for (j in 0 until n) {
                        if (mask and (1 shl j) != 0 && i != j) {
                            val cost = adjacencies[j][i] + (dp[mask xor (1 shl i)][j] ?: continue)
                            if (dp[mask][i] == null || cost < dp[mask][i]!!) dp[mask][i] = cost
                        }
                    }
                }
            }
        }

        val last = dp[(1 shl n) - 1].toList().map { it ?: Int.MAX_VALUE }
        return last.min() to last.withIndex().minOf { it.value + adjacencies[it.index][0] }
    }
}