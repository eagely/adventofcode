package solutions.y2021

import Solution
import utils.Utils.after
import utils.Utils.join
import utils.Utils.permutations
import utils.Utils.rl
import utils.Utils.split
import java.io.File

class Day8 : Solution(2021) {

    override fun solvePart1(input: File) = input.rl().flatMap { it.after(" | ").split() }.filter { it.length in 2..4 || it.length == 7 }.size

    override fun solvePart2(input: File): Any {
        val entries = input.readLines().map {
            val (patterns, output) = it.split(" | ").map { it.split(" ") }
            patterns to output
        }

        val solutions = "abcdefg".toList().permutations().map { it.join() }
2
        return entries.sumOf { (patterns, output) ->
            lateinit var mapping: Map<Char, Char>
            val segmap = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")

            for (perm in solutions) {
                val shift = "abcdefg".zip(perm).toMap()
                if (patterns.all { pattern -> segmap.any { it.toSet() == pattern.map { shift[it] }.join().toSortedSet() } }) {
                    mapping = shift
                    break
                }
            }

            output.map { segmap.indexOf(it.map { mapping[it] }.sortedBy { it!!.code }.join()) }.join().toInt()
        }
    }
}