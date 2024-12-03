package solutions.y2024

import Solution
import utils.extractNumbersSeparated
import utils.product
import utils.remove
import utils.text
import java.io.File

class Day3 : Solution(2024) {

    override fun solvePart1(input: File) = input.text.extractMul()

    override fun solvePart2(input: File) = input.text.remove(Regex("""don't\(\).*?(?=do\(\)|$)""", RegexOption.DOT_MATCHES_ALL)).extractMul()

    private fun String.extractMul() = Regex("""mul\((\d+),(\d+)\)""").findAll(this).sumOf { it.value.extractNumbersSeparated().product() }
}