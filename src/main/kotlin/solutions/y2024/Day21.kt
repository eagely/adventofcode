package solutions.y2024

import Solution
import utils.grid.Grid
import utils.lines
import utils.movement.Direction
import utils.permutations
import java.io.File

class Day21 : Solution(2024) {
    private val keypad = Grid.of(listOf("789", "456", "123", " 0A", " ^@", "<v>")).data.toList().associate { it.second to it.first }
    private val cache = mutableMapOf<Triple<String, Int, Int>, Long>()

    private fun dp(sequence: String, limit: Int, depth: Int): Long {
        val key = Triple(sequence, depth, limit)
        return cache.getOrPut(key) {
            sequence.fold(Pair(if (depth == 0) keypad['A']!! else keypad['@']!!, 0L)) { (pos, sum), char ->
                val next = keypad[char]!!
                val (dx, dy) = next - pos
                val paths = ((if (dx < 0) "^".repeat(-dx) else "v".repeat(dx)) + if (dy < 0) "<".repeat(-dy) else ">".repeat(dy)).permutations()
                    .filter { path -> path.asSequence().runningFold(pos) { pos, dir -> pos + Direction.of(dir) }.all { it in keypad.values } }.map { "$it@" }.ifEmpty { listOf("@") }
                next to (sum + if (depth == limit) paths.minOf { it.length }.toLong() else paths.minOfOrNull { dp(it, limit, depth + 1) } ?: paths.minOf { it.length }.toLong())
            }.second
        }
    }

    private fun solve(input: File, limit: Int) = input.lines.filter { it.isNotEmpty() }.sumOf { code -> dp(code, limit, 0) * code.filter { it.isDigit() }.toLong() }

    override fun solvePart1(input: File) = solve(input, 2)

    override fun solvePart2(input: File) = solve(input, 25)
}