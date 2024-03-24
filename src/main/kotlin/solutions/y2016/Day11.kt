package solutions.y2016

import Solution
import utils.after
import utils.annotations.NoTest
import utils.lines
import utils.remove
import utils.zipWithAllUnique
import java.io.File
import kotlin.math.min

// Untested Heuristic, if the answer is wrong or -1, then remove all if checks in the move function besides the first
class Day11 : Solution(2016) {
    data class Combination(val chip: Int, val generator: Int) : Comparable<Combination> {
        override fun compareTo(other: Combination): Int {
            return this.chip.compareTo(other.chip) + this.generator.compareTo(other.generator)
        }
        override fun toString(): String {
            return "($chip, $generator)"
        }
    }

    data class State(val floor: Int, val pairs: List<Combination>) {
        override fun hashCode(): Int {
            return floor + pairs.hashCode()
        }
        override fun equals(other: Any?): Boolean {
            return other is State && floor == other.floor && pairs.sorted() == other.pairs.sorted()
        }
        override fun toString(): String {
            return "[$floor, $pairs]"
        }
    }

    private fun State.isValid() = this.pairs.filter { it.chip != it.generator }.none { f -> this.pairs.any { it.generator == f.chip } }

    private fun State.move(df: Int): List<State> {
        val next = floor + df
        if (next !in pairs.minOf { min(it.chip, it.generator) }..3) return emptyList()
        val chips = pairs.withIndex().filter { it.value.chip == floor }.map { it.index }
        val generators = pairs.withIndex().filter { it.value.generator == floor }.map { it.index }
        val res = mutableListOf<State>()
        if (df == 1) {
            if (chips.size >= 2)
                res.addAll(chips.zipWithAllUnique().map { (a, b) -> State(next, pairs.mapIndexed { i, p -> if (i == a) Combination(next, p.generator) else if (i == b) Combination(next, p.generator) else p }) }.filter { it.isValid() })
            if (generators.size >= 2)
                res.addAll(generators.zipWithAllUnique().map { (a, b) -> State(next, pairs.mapIndexed { i, p -> if (i == a) Combination(p.chip, next) else if (i == b) Combination(p.chip, next) else p }) }.filter { it.isValid() })
            if (chips.size + generators.size >= 2)
                res.addAll(chips.zip(generators).map { (c, g) -> State(next, pairs.mapIndexed { i, p -> if (i == c) Combination(next, p.generator) else if (i == g) Combination(p.chip, next) else p }) }.filter { it.isValid() })
            if (res.isEmpty()) {
                res.addAll(chips.map { State(next, pairs.mapIndexed { i, p -> if (i == it) Combination(next, p.generator) else p }) }.filter { it.isValid() })
                res.addAll(generators.map { State(next, pairs.mapIndexed { i, p -> if (i == it) Combination(p.chip, next) else p }) }.filter { it.isValid() })
            }
        }
        if (df == -1) {
            res.addAll(chips.map { State(next, pairs.mapIndexed { i, p -> if (i == it) Combination(next, p.generator) else p }) }.filter { it.isValid() })
            res.addAll(generators.map { State(next, pairs.mapIndexed { i, p -> if (i == it) Combination(p.chip, next) else p }) }.filter { it.isValid() })
            if (res.isEmpty()) {
                if (chips.size >= 2)
                    res.addAll(chips.zipWithAllUnique().map { (a, b) -> State(next, pairs.mapIndexed { i, p -> if (i == a) Combination(next, p.generator) else if (i == b) Combination(next, p.generator) else p }) }.filter { it.isValid() })
                if (generators.size >= 2)
                    res.addAll(generators.zipWithAllUnique().map { (a, b) -> State(next, pairs.mapIndexed { i, p -> if (i == a) Combination(p.chip, next) else if (i == b) Combination(p.chip, next) else p }) }.filter { it.isValid() })
                if (chips.size + generators.size >= 2)
                    res.addAll(chips.zip(generators).map { (c, g) -> State(next, pairs.mapIndexed { i, p -> if (i == c) Combination(next, p.generator) else if (i == g) Combination(p.chip, next) else p }) }.filter { it.isValid() })
            }
        }
        return res.distinct().filter { it.isValid() }
    }

    @NoTest
    override fun solvePart1(input: File) = State(0, parse(input)).solve()

    @NoTest
    override fun solvePart2(input: File) = State(0, parse(input) + Combination(0, 0) + Combination(0, 0)).solve()

    private fun State.solve(): Int {
        val queue = ArrayDeque(listOf(this to 0))
        val visited = mutableSetOf<State>()

        while (queue.isNotEmpty()) {
            val (cur, steps) = queue.removeFirst()

            if (cur in visited) continue
            visited.add(cur)

            if (cur.pairs.all { it.chip == 3 && it.generator == 3 }) return steps

            queue.addAll(cur.move(1).union(cur.move(-1)).map { it to steps + 1 })
        }

        return -1
    }

    private fun parse(input: File): List<Combination> {
        val lines = input.lines
        val elements = mutableListOf<String>()
        lines.forEach { Regex("(?<=a )\\w+(?=-compatible microchip| generator)").findAll(it).forEach { elements.add(it.value) } }
        val start = lines.map { it.after("contains a ").remove("-compatible", ".").split(", and a ", ", a ", " and a ") }
        return elements.distinct().map { e -> Combination(start.indexOfFirst { "$e microchip" in it }, start.indexOfFirst { "$e generator" in it }) }
    }
}