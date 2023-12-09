package solutions.y2023

import Solution
import utils.Utils.dropBlanks
import utils.Utils.rl
import java.io.File

class Day9 : Solution(2023) {
    override fun solvePart1(input: File) = input.rl().sumOf { line -> generateSequence(line.split(" ").dropBlanks().map { it.toLong() }) { seq -> seq.zipWithNext { a, b -> b - a }.takeIf { it.any { diff -> diff != 0L } } }.fold(0L) { acc, seq -> acc + seq.last() } }

    override fun solvePart2(input: File) = input.rl().sumOf { line -> generateSequence(line.split(" ").dropBlanks().map { it.toLong() }) { seq -> seq.zipWithNext { a, b -> b - a }.takeIf { it.any { diff -> diff != 0L } } }.toList().asReversed().fold(0L) { acc, seq -> seq.first() - acc } }
}