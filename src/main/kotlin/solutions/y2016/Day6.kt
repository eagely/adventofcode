package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day6 : Solution(2016) {

    override fun solvePart1(input: File): Any {
        val lines = input.lines
        var out = ""
        repeat(lines[0].length) {i ->
            out += lines.map { it[i] }.counts().maxBy { it.value }.key
        }
        return out
    }

    override fun solvePart2(input: File): Any {
        val lines = input.lines
        var out = ""
        repeat(lines[0].length) {i ->
            out += lines.map { it[i] }.counts().minBy { it.value }.key
        }
        return out
    }
}