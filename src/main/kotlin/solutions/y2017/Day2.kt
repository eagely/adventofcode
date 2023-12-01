package solutions.y2017

import Solution
import utils.Utils.rl
import utils.Utils.zipWithAll
import java.io.File

class Day2 : Solution(2017) {
    override fun solvePart1(input: File) = input.rl().map { it.split(" ").map { s -> s.toInt() } }.sumOf { it.max() - it.min() }

    override fun solvePart2(input: File) = input.rl().map { it.split(" ").map { s -> s.toInt() } }.sumOf { line -> line.zipWithAll().filter { it.first % it.second == 0 }.sumOf { it.first / it.second } }
}
