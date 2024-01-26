package solutions.y2021

import Solution
import utils.Utils.rt
import java.io.File

class Day6 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val fish = input.rt().split(',').map { it.toInt() }
        val counts = (0..8).map { f -> fish.count { it == f }.toLong() }.toLongArray()
        return simulate(counts, 80)
    }

    override fun solvePart2(input: File): Any {
        val fish = input.rt().split(',').map { it.toInt() }
        val counts = (0..8).map { f -> fish.count { it == f }.toLong() }.toLongArray()
        return simulate(counts, 256)
    }

    private fun simulate(counts: LongArray, days: Int): Long {
        repeat(days) {
            val news = counts[0]
            for (i in 0..7) {
                counts[i] = counts[i+1]
            }
            counts[8] = news
            counts[6] += news
        }
        return counts.sum()
    }
}