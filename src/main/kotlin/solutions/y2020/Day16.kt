package solutions.y2020

import Solution
import utils.Utils.after
import utils.Utils.before
import utils.Utils.extractNumbersSeparated
import utils.Utils.product
import utils.Utils.rl
import utils.Utils.sdnl
import utils.Utils.swd
import utils.annotations.NoTest
import java.io.File

class Day16 : Solution(2020) {

    override fun solvePart1(input: File) = parse(input).let { it.second.flatten().filter { t -> it.first.none { it.second.any { t in it } } }.sum()}

    @NoTest
    override fun solvePart2(input: File): Any {
        val (fields, tickets) = parse(input)
        val mappings = mutableMapOf<Int, Int>()
        tickets.filter { t -> t.all { v -> fields.any { it.second.any { v in it } } } }.let { it.first().indices.map { col -> it.map { it[col] } }.mapIndexed { i, vs -> i to fields.filter { vs.all { v -> it.second.any { v in it } } } }.sortedBy { it.second.size } }.forEach { mappings[it.first] = fields.indexOf(it.second.first { field -> fields.indexOf(field) !in mappings.values }) }
        return mappings.entries.filter { it.value < 6 }.map { tickets.first()[it.key].toLong() }.product()
    }

    private fun parse(input: File) = input.sdnl().first().split("\n").map { it.before(":") to it.after(": ").split(" or ").map { it.before("-").toInt()..it.after("-").toInt() } } to input.rl().swd().map { it.extractNumbersSeparated() }
}