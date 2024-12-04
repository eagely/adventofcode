package solutions.y2024

import Solution
import utils.Utils.join
import utils.lines
import java.io.File

class Day4 : Solution(2024) {

    override fun solvePart1(input: File): Any {
        val lines = input.lines
        val rows = lines.size
        val cols = lines[0].length
        return lines.mapIndexed { r, rv ->
            rv.mapIndexed { c, _ ->
                listOf(
                    r + 3 < rows && c + 3 < cols && (0..3).map { lines[r + it][c + it] }.join() in "XMAS SAMX",
                    r + 3 < rows && (0..3).map { lines[r + it][c] }.join() in "XMAS SAMX",
                    c + 3 < cols && lines[r].substring(c, c + 4) in "XMAS SAMX",
                    r - 3 >= 0 && c + 3 < cols && (0..3).map { lines[r - it][c + it] }.join() in "XMAS SAMX"
                ).count { it }
            }.sum()
        }.sum()
    }

    override fun solvePart2(input: File): Any {
        val lines = input.lines
        val rows = lines.size
        val cols = lines[0].length
        return lines.mapIndexed { r, rv ->
            rv.filterIndexed { c, _ ->
                r + 2 < rows && c + 2 < cols && (0..2).map { lines[r + it][c + it] }.join() in "MAS SAM" &&
                        (0..2).map { lines[r + 2 - it][c + it] }.join() in "MAS SAM"
            }.length
        }.sum()
    }
}