package solutions.y2023

import Solution
import utils.Utils.extractNumbers
import utils.Utils.rl
import java.io.File

class Day2 : Solution(2023) {
    override fun solvePart1(input: File): Any {
        val lines = input.rl().mapIndexed { i, line ->
            val subgames = line.split(":")[1].split(";").map { subgame ->
                val extractor: (String) -> Int = { color -> "\\d+ $color".toRegex().findAll(subgame).map { it.value.extractNumbers().toInt() }.sum() }
                listOf(extractor("red"), extractor("green"), extractor("blue"))
            }
            i+1 to subgames
        }
        return lines.filterNot { it.second.any { s -> s[0] > 12 || s[1] > 13 || s[2] > 14 } }.sumOf { it.first }
    }

    override fun solvePart2(input: File): Any {
        val lines = input.rl().mapIndexed { i, line ->
            val subgames = line.split(":")[1].split(";").map { subgame ->
                val extractor: (String) -> Int = { color -> "\\d+ $color".toRegex().findAll(subgame).map { it.value.extractNumbers().toInt() }.sum() }
                listOf(extractor("red"), extractor("green"), extractor("blue"))
            }
            i+1 to subgames
        }
        return lines.sumOf { it.second.maxOf { s -> s[0] } * it.second.maxOf { s -> s[1] } * it.second.maxOf { s -> s[2] } }
    }
}
