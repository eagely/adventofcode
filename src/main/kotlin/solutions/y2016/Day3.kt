package solutions.y2016

import Solution
import utils.Utils.extractNumbersSeparated
import utils.Utils.rl
import java.io.File

class Day3 : Solution(2016) {

    override fun solvePart1(input: File) = input.rl().map { it.extractNumbersSeparated() }.count { it[0] + it[1] > it[2] && it[0] + it[2] > it[1] && it[1] + it[2] > it[0] }

    override fun solvePart2(input: File) = input.rl().asSequence().map { it.extractNumbersSeparated() }.chunked(3).map { listOf(listOf(it[0][0], it[1][0], it[2][0]), listOf(it[0][1], it[1][1], it[2][1]), listOf(it[0][2], it[1][2], it[2][2])) }.flatten().count { it[0] + it[1] > it[2] && it[0] + it[2] > it[1] && it[1] + it[2] > it[0] }
}