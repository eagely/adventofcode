package solutions.y2016

import Solution
import java.io.File
import utils.*
import utils.point.Point

class Day22 : Solution(2016) {

    override fun solvePart1(input: File) = input.lines.drop(2).zipWithAllIfItsNonAssociative().count { (a, b) -> a.extractNumbersSeparated()[3] in 1..b.extractNumbersSeparated()[4] }

    data class Drive(val pos: Point, var used: Int)

    override fun solvePart2(input: File): Any {
        val lines = input.lines.drop(2).map { it.extractNumbersSeparated().let { Drive(Point(it[1], it[0]), it[3]) } }
        val empty = lines.first { it.used == 0 }.pos
        val walls = lines.filter { it.used > 250 }.map { it.pos.y }
        return (lines.maxOf { it.pos.y } - 1) * 5 + empty.x + empty.y + walls.size - walls.min() + 1
    }
}