package solutions.y2023

import Solution
import utils.Utils.after
import utils.Utils.before
import utils.Utils.extractNumbers
import utils.Utils.getPolygonArea
import utils.Utils.rl
import utils.movement.Direction
import utils.point.LongPoint
import java.io.File

class Day18 : Solution(2023) {

    override fun solvePart1(input: File) = getArea(input.rl().map { it.substringBeforeLast(" ").split(" ").let { Direction.of(it[0][0]) to it[1].toLong() } })

    @OptIn(ExperimentalStdlibApi::class)
    override fun solvePart2(input: File): Any {
        val instr = input.rl().map { it.after('#').before(')').let { Direction.of(it.extractNumbers().last()) to it.take(5).hexToLong() } }
        return getArea(instr)
    }

    private fun getArea(input: List<Pair<Direction, Long>>): Long {
        var b = 0L
        var cur = LongPoint.ORIGIN
        val points = mutableListOf<LongPoint>()
        for ((dir, steps) in input) {
            cur += LongPoint(dir.toLongPoint().x * steps, dir.toLongPoint().y * steps)
            b += steps
            points.add(cur)
        }
        return getPolygonArea(points).toLong() - b / 2 + 1 + b
    }
}