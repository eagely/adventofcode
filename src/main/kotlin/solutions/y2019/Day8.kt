package solutions.y2019

import Solution
import utils.join
import utils.text
import java.io.File

class Day8 : Solution(2019) {

    override fun solvePart1(input: File) = input.text.chunked(150).minBy { it.count { c -> c == '0' } }.let { it.count { c -> c == '1' } * it.count { c -> c == '2' } }

    override fun solvePart2(input: File) =
        "\n" + input.text.chunked(150).reduce { acc, s -> acc.zip(s).map { (a, b) -> if (a == '2') b else a }.join() }.chunked(25).join("\n").replace("0", " ").replace("1", "█")
}