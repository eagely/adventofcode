package solutions.y2018

import Solution
import utils.*
import utils.annotations.NoTest
import java.io.File

class Day12 : Solution(2018) {

    @NoTest
    override fun solvePart1(input: File): Any {
        var (rules, plants) = parse(input)
        repeat(20) {
            plants = generation(rules, plants)
        }
        return plants.filter { it.value }.keys.sum()
    }

    @NoTest
    override fun solvePart2(input: File): Any {
        var (rules, plants) = parse(input)
        var equalDiffs = 0
        var gen = 0
        var diff = 0
        var old = 0
        while (equalDiffs < 10) { // 10 was chosen arbitrarily, although it should be safe since 2 was enough for my input
            val count = plants.filter { it.value }.keys.sum()
            plants = generation(rules, plants)
            diff = plants.filter { it.value }.keys.sum() - count
            if (diff == old)
                equalDiffs++
            else
                equalDiffs = 0
            old = diff
            gen++
        }
        return plants.filter { it.value }.keys.sum() + diff * (50000000000 - gen)
    }

    private fun generation(rules: Map<List<Boolean>, Boolean>, plants: Map<Int, Boolean>) = plants.filterValues { it }.keys.let { it.min() - 2..it.max() + 2 }.associateWith { p -> rules[(-2..2).map { plants[p + it] ?: false }] ?: (plants[p] ?: false) }

    private fun parse(input: File) = input.lines.let { lines -> lines.drop(2).associate { it.before(' ').toList().map { it == '#' } to (it.last() == '#') } to lines.first().after(": ").toList().mapIndexed { i, it -> i to (it == '#') }.toMap() }
}