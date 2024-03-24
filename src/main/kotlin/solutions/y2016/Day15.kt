package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day15 : Solution(2016) {

    override fun solvePart1(input: File) = input.lines.map { it.extractNumbersSeparated().let { (i, m, _, r) -> m to r + i } }.let { chineseRemainder(it.map { it.first }, it.map { it.first - it.second })  }

    override fun solvePart2(input: File) = (input.lines.map { it.extractNumbersSeparated().let { (i, m, _, r) -> m to r + i } } + (11 to 7)).let { chineseRemainder(it.map { it.first }, it.map { it.first - it.second })  }
}