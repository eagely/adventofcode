package solutions.y2021

import Solution
import utils.Utils.rl
import utils.Utils.zipWithAll
import utils.line.Line
import utils.point.Point
import java.io.File

class Day5 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val lines = input.rl().map { it.split(" -> ").map { Point.of(it) }.let { Line(it[0], it[1]) } }.filter { it.start.x == it.end.x || it.start.y == it.end.y }
        val overlaps = HashSet<Point>()
        for (comb in lines.zipWithAll()) overlaps.addAll(comb.first.intersection(comb.second))
        return overlaps.size
    }

    override fun solvePart2(input: File): Any {
        val lines = input.rl().map { it.split(" -> ").map { Point.of(it) }.let { Line(it[0], it[1]) } }
        val overlaps = HashSet<Point>()
        for (comb in lines.zipWithAll()) overlaps.addAll(comb.first.intersection(comb.second))
        return overlaps.size
    }
}