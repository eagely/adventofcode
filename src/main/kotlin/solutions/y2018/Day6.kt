package solutions.y2018

import Solution
import utils.*
import utils.annotations.NoTest
import utils.point.Point
import java.io.File

class Day6 : Solution(2018) {

    @NoTest
    override fun solvePart1(input: File): Any {
        val coords = input.lines.map { Point.of(it.remove(' ')).invert() }
        val mx = coords.maxOf { it.x }
        val my = coords.maxOf { it.y }

        val amts = coords.associateWith { 0 }.toMutableMap()
        repeat(mx) { x ->
            repeat(my) { y ->
                val p = x p y
                val distances = coords.map(p::manhattanDistance)
                val min = distances.min()
                if (distances.count(min::equals) == 1) {
                    val closest = coords.first { it.manhattanDistance(p) == min }
                    amts[closest] = amts[closest]!! + 1
                }
            }
        }
        val borders = ((0..<mx).flatMap { x -> listOf(coords.minBy { it.manhattanDistance(x p 0) }, coords.minBy { it.manhattanDistance(x p my - 1) }) } + (0..<my).flatMap { y -> listOf(coords.minBy { it.manhattanDistance(0 p y) }, coords.minBy { it.manhattanDistance(mx - 1 p y) }) }).toSet()
        return amts.filter { it.key !in borders }.maxOf { it.value }
    }

    @NoTest
    override fun solvePart2(input: File): Any {
        val coords = input.lines.map { Point.of(it.remove(' ')) }
        return (0..coords.maxOf { it.x }).sumOf { x -> (0..coords.maxOf { it.y }).count { y -> coords.sumOf { it.manhattanDistance(x p y) } < 10000 } }
    }
}