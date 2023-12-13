package solutions.y2023

import Solution
import utils.Utils.sdnl
import java.io.File

class Day13 : Solution(2023) {
    
    override fun solvePart1(input: File) = input.sdnl().sumOf { findMirror(it.lines()) * 100 + findMirror(invert(it.lines())) }

    override fun solvePart2(input: File) = input.sdnl().sumOf { findMirror(it.lines(), 1) * 100 + findMirror(invert(it.lines()), 1) }

    private fun findMirror(grid: List<String>, smudges: Int = 0) = (1 until grid.size).firstOrNull { grid.take(it).asReversed().zip(grid.drop(it)) { a, b -> a.zip(b).count { (a, b) -> a != b } }.sum() == smudges } ?: 0

    private fun invert(grid: List<String>) = List(grid.first().length) { i -> grid.joinToString("") { it[i].toString() } }
}