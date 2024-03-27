package solutions.y2015

import Solution
import java.io.File
import utils.*

class Day5 : Solution(2015) {

    override fun solvePart1(input: File) = input.lines.count { it.count { c -> c in "aeiou" } >= 3 && it.zipWithNext().any { (a, b) -> a == b } && !(it.contains("ab") || it.contains("cd") || it.contains("pq") || it.contains("xy")) }

    override fun solvePart2(input: File) = input.lines.count { s -> s.windowed(2).withIndex().any { (i, p) -> s.indexOf(p, i + 2) >= 0 } && s.windowed(3).any { it[0] == it[2] } }
}