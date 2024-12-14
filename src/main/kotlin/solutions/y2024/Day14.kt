package solutions.y2024

import Solution
import utils.annotations.NoTest
import utils.extractNegativesSeparated
import utils.lines
import utils.pm
import utils.point.Point
import utils.product
import java.io.File

class Day14 : Solution(2024) {

    private data class Robot(var p: Point, var v: Point)

    private fun parse(input: File) = input.lines.map { line -> line.extractNegativesSeparated().let { Robot(Point(it[0], it[1]), Point(it[2], it[3])) } }

    @NoTest
    override fun solvePart1(input: File): Any {
        val robots = parse(input)
        val w = 101
        val h = 103
        val quadrants = MutableList(4) { 0 }
        val mx = w / 2
        val my = h / 2

        for (robot in robots) {
            val (p, v) = robot
            p.x = (p.x + 100 * v.x) pm w
            p.y = (p.y + 100 * v.y) pm h

            quadrants[when {
                robot.p.x > mx && robot.p.y < my -> 0
                robot.p.x < mx && robot.p.y < my -> 1
                robot.p.x < mx && robot.p.y > my -> 2
                robot.p.x > mx && robot.p.y > my -> 3
                else -> continue
            }]++
        }
        return quadrants.product()
    }

    @NoTest
    override fun solvePart2(input: File): Any {
        val robots = parse(input)
        val w = 101
        val h = 103

        var i = 0
        while (true) {
            i++

            for (robot in robots) {
                val (p, v) = robot
                robot.p.x = (p.x+v.x) pm w
                robot.p.y = (p.y+v.y) pm h
            }

            if (robots.distinctBy { it.p }.size == robots.size) return i
        }
    }
}