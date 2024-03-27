package solutions.y2015

import Solution
import java.io.File
import utils.*

class Day8 : Solution(2015) {

    override fun solvePart1(input: File) = input.lines.let { lines -> lines.join().length - lines.map { it.replace("\\\\", "0").replace("\\\"", "0").replace(Regex("""\\x[0-9a-f]{2}"""), "0").drop(1).dropLast(1) }.join().length }

    override fun solvePart2(input: File) = input.lines.let { lines -> lines.map { "\"" + it.replace("\\", "\\\\").replace("\"", "\\\"") + "\"" }.join().length - lines.join().length }
}