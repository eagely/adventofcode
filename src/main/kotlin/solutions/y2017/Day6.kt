package solutions.y2017

import Solution
import java.io.File

class Day6 : Solution(2017) {
    override fun solvePart1(input: File): Any {
        val blocks = input.readText().trim().split("\\s+".toRegex()).map { it.toInt() }.toMutableList()
        val seen = mutableSetOf<List<Int>>()
        while (seen.add(blocks)) {
            redistribute(blocks)
        }
        return seen.size
    }

    override fun solvePart2(input: File): Any {
        val blocks = input.readText().trim().split("\\s+".toRegex()).map { it.toInt() }.toMutableList()
        val seen = mutableMapOf<List<Int>, Int>()
        var step = 0
        while (!seen.containsKey(blocks)) {
            seen[blocks] = step++
            redistribute(blocks)
        }
        return step - seen[blocks]!!
    }

    private fun redistribute(blocks: MutableList<Int>) {
        var (amt, idx) = blocks.max().let { it to blocks.indexOf(it) }
        blocks[idx] = 0
        while (amt > 0) {
            idx = ++idx % blocks.size
            blocks[idx]++
            amt--
        }
    }
}