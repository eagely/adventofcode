package solutions.y2015

import Solution
import java.io.File
import utils.*
import utils.annotations.NoTest
import kotlin.math.max

class Day15 : Solution(2015) {

    @NoTest
    override fun solvePart1(input: File): Any {
        val ingredients = input.lines.map { it.extractNegativesSeparated().dropLast(1) }

        var max = 0
        for (a in 0..100) {
            for (b in 0..100 - a) {
                for (c in 0..100 - a - b) {
                    val teaspoons = listOf(a, b, c, 100 - a - b - c)
                    max = max(max, (0..3).map { i -> max(0, (0..3).sumOf { j -> ingredients.mapIndexed { i, c -> c.map { it * teaspoons[i] } }[j][i] }) }.product())
                }
            }
        }

        return max
    }


    @NoTest
    override fun solvePart2(input: File): Any {
        val ingredients = input.lines.map { it.extractNegativesSeparated() }

        var max = 0
        for (a in 0..100) {
            for (b in 0..100 - a) {
                for (c in 0..100 - a - b) {
                    val teaspoons = listOf(a, b, c, 100 - a - b - c)

                    if (ingredients.flatMap { it.takeLast(1) }.mapIndexed { i, c -> c * teaspoons[i] }.sum() == 500)
                        max = max(max, (0..3).map { i -> max(0, (0..3).sumOf { j -> ingredients.mapIndexed { i, c -> c.dropLast(1).map { it * teaspoons[i] } }[j][i] }) }.product())
                }
            }
        }

        return max
    }
}