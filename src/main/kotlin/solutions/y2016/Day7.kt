package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day7 : Solution(2016) {

    override fun solvePart1(input: File) = input.lines.count { it.split("[", "]").withIndex().partition { (i, _) -> i % 2 == 0 }.let { (outside, inside) -> outside.joinToString(" ") { it.value }.containsAbba() && !inside.joinToString(" ") { it.value }.containsAbba() } }

    override fun solvePart2(input: File) = input.lines.count { it.split("[", "]").withIndex().partition { (i, _) -> i % 2 == 0 }.let { (outside, inside) -> outside.joinToString(" ") { it.value }.windowed(3).filter { it[0] == it[2] && it[0] != it[1] }.any { aba -> inside.joinToString(" ") { it.value }.windowed(3).filter { it[0] == it[2] && it[0] != it[1] }.any { it[0] == aba[1] && it[1] == aba[0] } } } }

    private fun String.containsAbba(): Boolean = this.windowed(4).any { it[0] == it[3] && it[1] == it[2] && it[0] != it[1] }
}