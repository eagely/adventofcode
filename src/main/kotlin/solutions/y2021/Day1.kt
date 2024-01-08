package solutions.y2021

import Solution
import utils.Utils.ril
import java.io.File

class Day1 : Solution(2021) {

    override fun solvePart1(input: File) = input.ril().windowed(2).count { (a, b) -> b > a }

    override fun solvePart2(input: File) = input.ril().windowed(3).windowed(2).count { (a, b) -> b.sum() > a.sum() }
}