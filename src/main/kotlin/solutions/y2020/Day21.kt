package solutions.y2020

import Solution
import utils.Utils.dropBrackets
import utils.Utils.rl
import utils.Utils.split
import java.io.File

class Day21 : Solution(2020) {

    override fun solvePart1(input: File) = parse(input).let { meals -> (meals.map { it.first }.flatten().toSet() - meals.map { it.second }.flatten().distinct().map { al -> meals.filter { al in it.second }.map { it.first.toSet() }.reduce { a, b -> a.intersect(b) } }.flatten().toSet()).sumOf { i -> meals.count { i in it.first } } }

    override fun solvePart2(input: File): Any {
        val meals = parse(input)
        var rem = meals.asSequence().map { it.second }.flatten().distinct().map { al -> al to meals.filter { al in it.second }.map { it.first.toSet() }.reduce { a, b -> a intersect b} }.sortedBy { it.second.size }.toList()
        val map = hashMapOf<String, String>()
        while (rem.isNotEmpty()) {
            val (al, ing) = rem.first()
            map[al] = ing.first()
            rem = rem.filter { it.first != al }.map { it.first to (it.second - ing) }.sortedBy { it.second.size }
        }
        return map.toSortedMap().values.joinToString(",")
    }

    private fun parse(input: File) = input.rl().map { it.dropBrackets().split(" contains ").let { it.first().split() to it.last().split(", ") } }
}