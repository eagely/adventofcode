package solutions.y2021

import Solution
import utils.Utils.rit
import java.io.File
import kotlin.math.abs
import kotlin.math.min

class Day7 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val pos = input.rit()
        var tot = Int.MAX_VALUE
        for (avg in pos.min()..pos.max())
            tot = min(tot, pos.map { abs(it - avg) }.sum())
        return tot
    }

    override fun solvePart2(input: File): Any {
        val pos = input.rit()
        var tot = Int.MAX_VALUE
        for (avg in pos.min()..pos.max())
            tot = min(tot, pos.map { abs(it - avg) * (abs(it - avg) + 1) / 2 }.sum())
        return tot
    }
}