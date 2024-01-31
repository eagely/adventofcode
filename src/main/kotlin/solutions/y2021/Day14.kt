package solutions.y2021

import Solution
import utils.Utils.counts
import utils.Utils.sdanl
import utils.Utils.sdnl
import java.io.File

class Day14 : Solution(2021) {

    override fun solvePart1(input: File) = input solve 10

    override fun solvePart2(input: File) = input solve 40

    private infix fun File.solve(rounds: Int): Long {
        val polymer = this.sdnl().first()
        val rules = this.sdanl().last().associate { it.split(" -> ").let { (pair, result) -> pair to result } }
        var pairs = polymer.windowed(2).counts().mapValues { it.value.toLong() }

        repeat(rounds) {
            val counts = mutableMapOf<String, Long>()
            pairs.forEach { (p, c) ->
                rules[p]?.let {
                    counts.merge(p.first() + it, c, Long::plus)
                    counts.merge(it + p.last(), c, Long::plus)
                }
            }
            pairs = counts
        }

        val counts = mutableMapOf<Char, Long>()
        pairs.forEach { (p, c) -> counts.merge(p.first(), c, Long::plus) }
        counts.merge(polymer.last(), 1L, Long::plus)

        return counts.maxOf { it.value } - counts.minOf { it.value }
    }
}