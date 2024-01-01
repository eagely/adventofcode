package solutions.y2020

import Solution
import utils.Utils.after
import utils.Utils.before
import utils.Utils.remove
import utils.Utils.rl
import java.io.File

class Day7 : Solution(2020) {

    override fun solvePart1(input: File): Any {
        val bags = parse(input)
        val seen = mutableSetOf<String>()
        var next = listOf("shiny gold")
        while (next.isNotEmpty()) {
            next = bags.filter { bag -> bag.value.any { it.second in next } }
                .map { it.key }.filterNot { it in seen }
                .also { seen.addAll(it) }
        }
        return seen.size
    }

    override fun solvePart2(input: File): Any {
        val bags = parse(input)
        fun countBags(bag: String): Int = 1 + bags[bag]!!.sumOf { it.first * countBags(it.second) }
        return countBags("shiny gold") - 1
    }

    private fun parse(input: File) = input.rl().associate { it.before(" bags") to it.after("contain ").remove(" bags", " bag", ".").split(", ").mapNotNull { it.split(" ", limit = 2).takeIf { "no" !in it }?.let { (num, type) -> num.toInt() to type } } }
}