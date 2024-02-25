package solutions.y2017

import Solution
import java.io.File
import utils.*

class Day4 : Solution(2017) {

    override fun solvePart1(input: File) = input.lines.map { it.split(" ") }.count { it.toSet().size == it.size }

    override fun solvePart2(input: File) = input.lines.map { it.split(" ").map { it.toSortedSet() } }.count { it.toSet().size == it.size }
}