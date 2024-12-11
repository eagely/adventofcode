package solutions.y2024

import Solution
import utils.extractLongsSeparated
import utils.halve
import utils.text
import java.io.File

class Day11 : Solution(2024) {

    override fun solvePart1(input: File) = input.text.extractLongsSeparated().sumOf { count(25, 0, it) }

    override fun solvePart2(input: File): Any {
        cache.clear()
        return input.text.extractLongsSeparated().sumOf { count(75, 0, it) }
    }

    private val cache = HashMap<Pair<Int, Long>, Long>()
    private fun count(limit: Int, rounds: Int, stone: Long): Long {
        if (rounds == limit) return 1
        cache[rounds to stone]?.let { return it }
        cache[rounds to stone] = if (stone == 0L) {
            count(limit, rounds + 1, 1)
        } else if (stone.toString().length % 2 == 0) {
            val (a, b) = stone.toString().halve()
            count(limit, rounds + 1, a.toLong()) + count(limit, rounds + 1, b.toLong())
        } else count(limit, rounds + 1, stone * 2024)
        return cache[rounds to stone]!!
    }
}