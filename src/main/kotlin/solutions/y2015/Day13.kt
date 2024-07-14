package solutions.y2015

import Solution
import java.io.File
import utils.*

class Day13 : Solution(2015) {

    data class Happiness(val person: String, val neighbor: String, val score: Int)

    override fun solvePart1(input: File): Any {
        val stats = input.lines.map { it.removeSuffix(".").split() }.map { Happiness(it.first(), it.last(),  it[3].toInt() * if(it[2] == "lose") -1 else 1) }
        return (stats.map { it.person }.distinct())
            .permutations()
            .maxOf { pr ->
                (pr.zipWithNext() + (pr.last() to pr.first()))
                    .sumOf { (p, n) -> stats.first { it.person == p && it.neighbor == n}.score + stats.first { it.person == n && it.neighbor == p}.score }
            }
    }

    override fun solvePart2(input: File): Any {
        val stats = input.lines.map { it.removeSuffix(".").split() }.map { Happiness(it.first(), it.last(),  it[3].toInt() * if(it[2] == "lose") -1 else 1) }
        return (stats.map { it.person }.distinct() + "me")
            .permutations()
            .maxOf { pr ->
            (pr.zipWithNext() + (pr.last() to pr.first()))
                .filterNot { it.first == "me" || it.second == "me" }
                .sumOf { (p, n) -> stats.first { it.person == p && it.neighbor == n}.score + stats.first { it.person == n && it.neighbor == p}.score }
        }
    }
}