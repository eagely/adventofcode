package solutions.y2017

import Solution
import java.io.File
import utils.*
import utils.movement.VerticalHexDirection
import utils.point.Point
import kotlin.math.max

class Day11 : Solution(2017) {

    override fun solvePart1(input: File): Any {
        val dirs = input.text.split(",").map { VerticalHexDirection.of(it) }
        var pos = Point(0, 0)
        for (dir in dirs) pos += dir.point
        return VerticalHexDirection.stepsToPoint(Point(0, 0), pos)
    }

    override fun solvePart2(input: File): Any {
        val dirs = input.text.split(",").map { VerticalHexDirection.of(it) }
        var pos = Point(0, 0)
        var max = 0
        for (dir in dirs) {
            pos += dir.point
            max = max(VerticalHexDirection.stepsToPoint(Point(0, 0), pos), max)
        }
        return max
    }
}