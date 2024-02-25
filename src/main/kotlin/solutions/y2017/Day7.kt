package solutions.y2017

import Solution
import utils.*
import java.io.File
import kotlin.math.abs

class Day7 : Solution(2017) {

    override fun solvePart1(input: File): Any {
        val lines = input.lines
        return lines.map { it.before(" ") }.first { it !in lines.filter { " -> " in it }.map { it.after(" -> ").split(", ") }.flatten() }
    }

    override fun solvePart2(input: File): Any {
        val lines = input.lines
        return findSillyGoofer(lines.map { it.before(" ") }.first { it !in lines.filter { " -> " in it }.map { it.after(" -> ").split(", ") }.flatten() },  lines.filter { " -> " in it }.associate { (it.before(" ") to it.substringAfter(" -> ").split(", ")) }, lines.associate { it.before(" ") to it.extractNumbers().toInt() }).second
    }

    private fun findSillyGoofer(name: String, connections: Map<String, List<String>>, weightMap: Map<String, Int>): Pair<Int, Int> {
        val weight = weightMap[name]!!
        if (connections.containsKey(name)) {
            val children = connections.getValue(name)
            val childWeights = children.map { findSillyGoofer(it, connections, weightMap) }
            if (childWeights.toSet().size > 1 && children.map { if (connections.containsKey(it)) connections[it] else listOf(it) }.map { it!!.map { findSillyGoofer(it, connections, weightMap) } }.all { it.isAllEqual() }) return childWeights.sumOf { it.first } to weightMap[children[childWeights.indexOf(childWeights.first { weight -> childWeights.count { it == weight } == 1 })]]!! - abs(childWeights.map { it.first }.let { it.max() - it.min() })
            return childWeights.sumOf { it.first } + weight to childWeights.sumOf { it.second }
        }
        else return weight to 0
    }
}