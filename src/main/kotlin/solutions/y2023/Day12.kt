package solutions.y2023

import Solution
import utils.Utils.dropBlanks
import utils.Utils.rl
import java.io.File

class Day12 : Solution(2023) {

    override fun solvePart1(input: File) = input.rl().sumOf { it.split(" ").let { count(it.first(), it[1].split(",").map(String::toInt)) } }

    override fun solvePart2(input: File) = input.rl().sumOf { it.split(" ").let { count("${it.first()}?".repeat(5).dropLast(1), "${it[1]},".repeat(5).split(",").dropBlanks().map(String::toInt)) } }

    private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
    private fun count(config: String, groups: List<Int>): Long {
        if (groups.isEmpty()) return if ("#" in config) 0 else 1
        if (config.isEmpty()) return 0

        return cache.getOrPut(config to groups) {
            var result = 0L
            if (config.first() in ".?")
                result += count(config.drop(1), groups)
            if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
                result += count(config.drop(groups.first() + 1), groups.drop(1))
            result
        }
    }
}
