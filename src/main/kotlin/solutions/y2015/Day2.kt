package solutions.y2015

import Solution
import utils.Utils.extractNumbersSeparated
import utils.Utils.product
import utils.Utils.rl
import java.io.File

class Day2 : Solution(2015) {

    override fun solvePart1(input: File) = input.rl().map { it.extractNumbersSeparated() }.map { Triple(2*it[0]*it[1], 2*it[1]*it[2], 2*it[2]*it[0]) }.sumOf { it.first + it.second + it.third + it.toList().min() / 2}

    override fun solvePart2(input: File) = input.rl().map { it.extractNumbersSeparated() }.sumOf { 2 * it.sorted().take(2).sum() + it.product() }
}
