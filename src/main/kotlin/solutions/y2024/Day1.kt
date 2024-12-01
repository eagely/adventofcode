package solutions.y2024

import Solution
import utils.extractNumbersSeparated
import utils.lines
import java.io.File
import kotlin.math.abs

class Day1 : Solution(2024) {

    override fun solvePart1(input: File): Any {
        val lines = input.lines.map{ it.extractNumbersSeparated() }
        val first = lines.map { it.first() }.sorted()
        val second = lines.map { it.last() }.sorted()

        return first.zip(second).sumOf { abs(it.first - it.second) }
    }

    override fun solvePart2(input: File): Any {
        val lines = input.lines.map{ it.extractNumbersSeparated() }
        val first = lines.map { it.first() }.sorted()
        val second = lines.map { it.last() }.sorted()

        return first.sumOf { left -> left * second.count { right -> right == left }}
    }
}