package solutions.y2022

import Solution
import java.io.File

class Day20 : Solution(2022) {
    data class Node(var index: Int, var n: Long)

    override fun solvePart1(input: File) = input.parse().decrypt()

    override fun solvePart2(input: File) = input.parse { it * 811589153 }.decrypt(10)

    private fun File.parse(transform: (Long) -> Long = { it }): MutableList<Node> = readLines().mapIndexed { i, n -> Node(i, transform(n.toLong())) }.toMutableList()

    private fun MutableList<Node>.decrypt(cycles: Int = 1): Long {
        repeat(cycles) {
            indices.forEach { i ->
                val pos = indexOfFirst { it.index == i }
                val node = removeAt(pos)
                add((pos + node.n).mod(size), node)
            }
        }
        return listOf(1000, 2000, 3000).sumOf { this[(indexOfFirst { z -> z.n == 0L } + it) % size].n }
    }
}
