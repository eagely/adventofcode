package solutions.y2020

import Solution
import utils.Utils.counts
import utils.Utils.rl
import utils.Utils.toChar
import java.io.File

class Day2 : Solution(2020) {

    override fun solvePart1(input: File) = input.rl().map { it.split(": ", " ", "-") }.count { s -> s[3].counts().any { it.key == s[2].toChar() && it.value >= s[0].toInt() && it.value <= s[1].toInt() } }

    override fun solvePart2(input: File) = input.rl().map { it.split(": ", " ", "-") }.count { s -> (s[3][s[0].toInt() - 1] == s[2].toChar()) xor (s[3][s[1].toInt() - 1] == s[2].toChar()) }
}