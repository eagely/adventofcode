package solutions.y2022

import Solution
import utils.point.Point
import java.io.File
import kotlin.math.abs

class Day9 : Solution(2022) {
    private fun moveKnot(knot: Point, direction: String) {
        when (direction) {
            "U" -> knot.y += 1
            "D" -> knot.y -= 1
            "L" -> knot.x -= 1
            "R" -> knot.x += 1
        }
    }

    override fun solvePart1(input: File): String {
        val commands = input.readLines()
        val pos = mutableSetOf<Point>()
        val head = Point(0, 0)
        val tail = Point(0, 0)

        for (commandLine in commands) {
            val (direction, distance) = commandLine.split(" ")
            repeat(distance.toInt()) {
                moveKnot(head, direction)
                val dx = head.x - tail.x
                val dy = head.y - tail.y
                val dif = abs(dx) + abs(dy)
                if (abs(dy) > 1 || dif > 2) tail.y += dy / abs(dy)
                if (abs(dx) > 1 || dif > 2) tail.x += dx / abs(dx)
                pos.add(tail.copy())
            }
        }
        return pos.size.toString()
    }

    override fun solvePart2(input: File): String {
        val commands = input.readLines()
        val pos = mutableSetOf<Point>()
        val knots = MutableList(10) { Point(0, 0) }

        for (commandLine in commands) {
            val (direction, distance) = commandLine.split(" ")
            repeat(distance.toInt()) {
                moveKnot(knots[0], direction)
                for (j in 1..<knots.size) {
                    val dx = knots[j - 1].x - knots[j].x
                    val dy = knots[j - 1].y - knots[j].y
                    val dif = abs(dx) + abs(dy)

                    if (abs(dy) > 1 || dif > 2) knots[j].y += dy / abs(dy)
                    if (abs(dx) > 1 || dif > 2) knots[j].x += dx / abs(dx)
                }
                pos.add(knots.last().copy())
            }
        }
        return pos.size.toString()
    }
}