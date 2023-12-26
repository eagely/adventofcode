package solutions.y2015

import Solution
import utils.Utils.rt
import utils.movement.Direction
import utils.point.Point
import java.io.File

class Day3 : Solution(2015) {

    override fun solvePart1(input: File): Any {
        val dirs = input.rt().map { Direction.of(it) }
        val visited = mutableListOf(Point(0, 0))
        dirs.forEach { visited.add(visited.last() + it) }
        return visited.distinct().size
    }

    override fun solvePart2(input: File): Any {
        val dirs = input.rt().map { Direction.of(it) }
        val santa = mutableListOf(Point(0, 0))
        val robo = mutableListOf(Point(0, 0))
        dirs.forEachIndexed { i, it -> if (i % 2 == 0) santa.add(santa.last() + it) else robo.add(robo.last() + it) }
        return santa.union(robo).size
    }
}
