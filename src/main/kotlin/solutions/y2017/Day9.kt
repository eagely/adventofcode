package solutions.y2017

import Solution
import utils.text
import java.io.File

class Day9 : Solution(2017) {

    override fun solvePart1(input: File) = dump(input).first

    override fun solvePart2(input: File) = dump(input).second

    private fun dump(input: File): Pair<Int, Int> {
        val it = input.text

        var nest = 0
        var g = 0
        var s = 0
        var skip = false
        var garbage = false
        for (c in it) {

            if (skip) {
                skip = false
                continue
            }

            if (garbage && c !in ">!") {
                g++
            }

            when (c) {
                '!' -> skip = true
                '<' -> garbage = true
                '>' -> garbage = false
                '{' -> if (!garbage) s += ++nest
                '}' -> if (!garbage) nest--
            }
        }

        return s to g
    }
}