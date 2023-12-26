package solutions.y2020

import Solution
import utils.Utils.product
import utils.Utils.ril
import utils.Utils.zipWithAllUnique
import java.io.File

class Day1 : Solution(2020) {
    override fun solvePart1(input: File): Any {
        return input.ril().zipWithAllUnique().first { it.first + it.second == 2020 }.let { it.first * it.second }
    }

    override fun solvePart2(input: File): Any {
        return input.ril().zipWithAllUnique(3).first { it.sum() == 2020 }.product()

    }
}
