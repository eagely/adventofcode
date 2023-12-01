package solutions.y2018

import Solution
import utils.Utils.consecutive
import utils.Utils.containsLength
import utils.Utils.duplicates
import utils.Utils.l
import utils.Utils.matching
import utils.Utils.mp
import utils.Utils.rl
import utils.Utils.s
import utils.Utils.zipWithAllUnique
import java.io.File

class Day2 : Solution(2018) {
    override fun solvePart1(input: File): Any {
        return input.rl().filter { it.duplicates().consecutive().containsLength(2) }.s * input.rl().filter { it.duplicates().consecutive().containsLength(3) }.s
    }

    override fun solvePart2(input: File): Any {
        return input.rl().zipWithAllUnique().filter { (it.first matching it.second).l == it.first.l - 1 } mp { it.first matching it.second }
    }
}
