package solutions.y2017

import Solution
import utils.extractNumbers
import utils.isPrime
import utils.lines
import utils.pow
import java.io.File

class Day23 : Solution(2017) {

    override fun solvePart1(input: File) = (input.lines.first().extractNumbers().toInt() - 2).pow(2)

    override fun solvePart2(input: File) = (input.lines.first().extractNumbers().toInt() * 100 + 100000).let { b -> (b..b + 17000 step 17).count { !it.isPrime() } }
}