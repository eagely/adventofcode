package solutions.y2023

import Solution
import utils.Utils.extractNumbers
import utils.Utils.rl
import java.io.File

class Day1 : Solution(2023) {
    override fun solvePart1(input: File) = input.rl().sumOf { (it.extractNumbers().first().toString() + it.extractNumbers().last()).toInt() }

    override fun solvePart2(input: File): Any {
        val digits = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )
        return input.rl().sumOf { line ->
            val start = digits.keys.mapNotNull { line.windowed(it.length).indexOf(it).let { index -> if (index == -1) null else digits[it]!! to index } } + digits.values.mapNotNull { line.indexOf(it.toString()).let { index -> if (index == -1) null else it to index } }
            val x = start.minByOrNull { (_, it) -> it }!!.first

            val end = digits.keys.mapNotNull { line.windowed(it.length).lastIndexOf(it).let { index -> if (index == -1) null else digits[it]!! to index } } + digits.values.mapNotNull { line.lastIndexOf(it.toString()).let { index -> if (index == -1) null else it to index } }
            val y = end.maxByOrNull { (_, it) -> it }!!.first

            x * 10 + y
        }
    }
}

