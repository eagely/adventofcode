package solutions.y2023

import Solution
import utils.Utils.extractNumbers
import utils.Utils.product
import utils.Utils.rl
import utils.Utils.split
import java.io.File

class Day6 : Solution(2023) {

    override fun solvePart1(input: File): Any {
        val (time, dist) = input.rl().map { it.split().drop(1).map { it.toInt() } }
        return time.map { (0..it).filter { t -> (it - t) * t > dist[time.indexOf(it)] }.size }.product()
    }

    override fun solvePart2(input: File): Any {
        val (time, dist) = input.rl().map { it.extractNumbers().toLong() }
        return (0L..time).filter { (time - it) * it > dist }.size
    }
}
