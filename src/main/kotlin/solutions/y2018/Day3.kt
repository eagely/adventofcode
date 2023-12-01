package solutions.y2018

import Solution
import utils.Utils.extractNumbers
import utils.Utils.rl
import utils.point.Point
import java.io.File

class Day3 : Solution(2018) {
    override fun solvePart1(input: File): Any {
        val l = input.rl()
        val pointCounts = mutableMapOf<Point, Int>()
        val ranges = mutableListOf<Pair<IntRange, IntRange>>()

        for (i in l) {
            val x = i.split(" @ ", ",", ": ", "x").map { it.extractNumbers().toInt() }
            val rx = x[2] until x[2] + x.last()
            val ry = x[1] until x[1] + x[3]
            ranges.add(rx to ry)
        }

        for ((xRange, yRange) in ranges) {
            for (x in xRange) {
                for (y in yRange) {
                    val point = Point(x, y)
                    pointCounts[point] = pointCounts.getOrDefault(point, 0) + 1
                }
            }
        }

        return pointCounts.count { it.value > 1 }
    }

    override fun solvePart2(input: File): Any {
        val l = input.rl()
        val pointCounts = mutableMapOf<Point, Int>()
        val ranges = mutableListOf<Pair<IntRange, IntRange>>()
        val claims = l.map { it.substringBefore("@").extractNumbers().toInt() }

        for (i in l) {
            val x = i.split(" @ ", ",", ": ", "x").map { it.extractNumbers().toInt() }
            val rx = x[2] until x[2] + x.last()
            val ry = x[1] until x[1] + x[3]
            ranges.add(rx to ry)
        }

        for ((xRange, yRange) in ranges) {
            for (x in xRange) {
                for (y in yRange) {
                    val point = Point(x, y)
                    pointCounts[point] = pointCounts.getOrDefault(point, 0) + 1
                }
            }
        }

        h@ for(i in claims) {
            for (x in ranges[i-1].first) {
                for (y in ranges[i-1].second) {
                    val point = Point(x, y)
                    if((pointCounts[point] ?: 0) > 1) continue@h
                }
            }
            return i
        }
        throw IllegalStateException()
    }
}
