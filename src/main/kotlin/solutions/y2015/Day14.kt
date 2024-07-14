package solutions.y2015

import Solution
import java.io.File
import utils.*
import utils.annotations.NoTest
import kotlin.math.min

class Day14 : Solution(2015) {

    data class Reindeer(val speed: Int, val endurance: Int, val rest: Int)

    @NoTest
    override fun solvePart1(input: File): Any {
        val reindeer = input.lines.map { it.extractNumbersSeparated().let { Reindeer(it[0], it[1], it[2]) } }
        val duration = 2503
        return reindeer.maxOf {
            val cycle = it.endurance + it.rest
            (duration / cycle) * it.speed * it.endurance + (it.speed * min(duration % cycle, it.endurance))
        }
    }

    data class Scoredeer(val speed: Int, val endurance: Int, val rest: Int, var score: Int, var distance: Int)

    @NoTest
    override fun solvePart2(input: File): Any {
        val reindeer = input.lines.map { it.extractNumbersSeparated().let { Scoredeer(it[0], it[1], it[2], 0, 0) } }

        for (now in 0..2503) {
            for (r in reindeer) {
                if (now % (r.endurance + r.rest) < r.endurance)
                    r.distance += r.speed
            }
            reindeer.filter { it.distance == reindeer.maxOf { it.distance } }.forEach { it.score++ }
        }
        return reindeer.maxOf { it.score }
    }
}