package solutions.y2017

import Solution
import utils.annotations.NoTest
import utils.join
import utils.lines
import java.io.File

class Day21 : Solution(2017) {

    @NoTest
    override fun solvePart1(input: File) = solve(input, 5)

    @NoTest
    override fun solvePart2(input: File) = solve(input, 18)

    private fun solve(input: File, iterations: Int): Int {
        val rules = input.lines.associate { line -> line.split(" => ").map { it.split('/') }.let { it.first() to it.last() } }
        var grid = listOf(".#.", "..#", "###")
        repeat(iterations) {
            grid = grid.enhance(rules)
        }
        return grid.join().count { it == '#' }
    }

    private fun List<String>.enhance(rules: Map<List<String>, List<String>>): List<String> {
        val cs = if (size % 2 == 0) 2 else 3
        val tiledSize = size / cs

        val grid = Array(tiledSize) { Array<List<String>>(tiledSize) { emptyList() } }

        for (i in indices step cs) {
            for (j in indices step cs) {
                grid[i / cs][j / cs] = (0..<cs).map { get(i + it).substring(j, j + cs) }.rotations().firstNotNullOf { rules[it] }
            }
        }

        return (0..<tiledSize).flatMap { i ->
            (0..<cs + 1).map { j ->
                grid[i].joinToString("") { it[j] }
            }
        }
    }

    private fun List<String>.rotations(): Set<List<String>> {
        val res = mutableSetOf<List<String>>()
        var cur = this
        repeat(4) {
            res.add(cur)
            res.add(cur.flip())
            cur = cur.rotate()
        }
        return res
    }

    private fun List<String>.rotate() = first().indices.map { c -> indices.map { r -> this[indices.last - r][c] }.join() }

    private fun List<String>.flip() = reversed()
}