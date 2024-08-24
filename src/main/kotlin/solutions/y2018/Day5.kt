package solutions.y2018

import Solution
import java.io.File
import utils.*

class Day5 : Solution(2018) {

    override fun solvePart1(input: File) = react(input.text)

    override fun solvePart2(input: File) = ('a'..'z').minOf { c -> react(input.text.filter { it.lowercaseChar() != c }) }

    private fun react(polymer: String) = polymer.fold("") { acc, c ->
        if (acc.isNotEmpty() && acc.last() != c && acc.last().equals(c, ignoreCase = true))
            acc.dropLast(1)
        else
            acc + c
    }.length
}