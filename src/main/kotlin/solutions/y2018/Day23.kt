package solutions.y2018

import Solution
import utils.extractNegativesSeparated
import utils.lines
import utils.point.Point3D
import java.io.File
import kotlin.math.abs
import kotlin.math.max

class Day23 : Solution(2018) {

    data class Bot(var pos: Point3D, val radius: Int)

    override fun solvePart1(input: File): Any {
        val bots = input.lines.map { line -> line.extractNegativesSeparated().let { n -> Bot(Point3D(n[0], n[1], n[2]), n[3]) } }
        return bots.maxBy { it.radius }.let { bot -> bots.count { bot.pos.manhattanDistance(it.pos) <= bot.radius } }
    }

    override fun solvePart2(input: File): Any {
        val bots = input.lines.map { line -> line.extractNegativesSeparated().let { n -> n.dropLast(1).sumOf { abs(it) } to n[3] } }
        val order = bots.flatMap { (d, r) -> listOf(max(0, d - r) to 1, d + r + 1 to -1) }
        var count = 0
        var max = 0
        var res = 0
        for ((dist, entering) in order.sortedBy { it.first }) {
            count += entering
            if (count > max) {
                max = count
                res = dist
            }
        }
        return res
    }
}