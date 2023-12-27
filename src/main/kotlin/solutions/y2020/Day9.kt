package solutions.y2020

import Solution
import utils.Utils.rll
import utils.Utils.zipWithAllUnique
import java.io.File

class Day9 : Solution(2020) {

    override fun solvePart1(input: File): Any {
        val lines = input.rll()
        val zipped = lines.zipWithAllUnique()
        return lines.filterIndexed { i, e -> i > 24 && zipped.none { (a, b) -> a + b == e } }.first()
    }

    override fun solvePart2(input: File): Any {
        val lines = input.rll()
        val zipped = lines.zipWithAllUnique()
        val invalid = lines.filterIndexed { i, e -> i > 24 && zipped.none { (a, b) -> a + b == e } }.first()
        var size = 2
        while (true) {
            val window = lines.windowed(size)
            val found = window.firstOrNull { it.sum() == invalid }
            if (found != null) return found.minOrNull()!! + found.maxOrNull()!!
            size++
        }
    }
}
