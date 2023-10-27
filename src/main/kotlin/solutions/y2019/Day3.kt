package solutions.y2019

import Solution
import utils.Utils.extractNumbers
import utils.point.Point
import java.io.File
import java.math.BigDecimal

class Day3 : Solution(2019) {
    override fun solvePart1(input: File): String {
        val line1 = getPathSet(input.readLines().first().split(","))
        val line2 = getPathSet(input.readLines().filterNot { it.isBlank() }.last().split(","))
        val matches = line1.intersect(line2).toHashSet()
        matches.remove(Point(0, 0))
        var dist: BigDecimal
        var smallest = BigDecimal(Long.MAX_VALUE)
        for(i in matches) {
            dist = i.x.abs() + i.y.abs()
            if(dist < smallest)
                smallest = dist
        }
        return smallest.toString()
    }

    override fun solvePart2(input: File): String {
        val line1 = getPathMap(input.readLines().first().split(","))
        val line2 = getPathMap(input.readLines().filterNot { it.isBlank() }.last().split(","))
        val matches = line1.keys.intersect(line2.keys).toHashSet()
        matches.remove(Point(0,0))
        var steps: BigDecimal
        var smallest = BigDecimal(Long.MAX_VALUE)
        for(i in matches) {
            steps = line1[i]!!.toBigDecimal() + line2[i]!!.toBigDecimal()
            if(steps < smallest)
                smallest = steps
        }
        return smallest.toString()
    }

    private fun getPathSet(input: List<String>): Set<Point> {
        var pos = Point(0, 0)
        val set = mutableSetOf(pos)
        for (i in input) {
            repeat(i.extractNumbers().toInt()) {
                when (i.first()) {
                    'U' -> pos += Point(0, 1)
                    'R' -> pos += Point(1, 0)
                    'D' -> pos += Point(0, -1)
                    'L' -> pos += Point(-1, 0)
                }
                set.add(pos)
            }
        }
        return set
    }

    private fun getPathMap(input: List<String>): Map<Point, Int> {
        var pos = Point(0, 0)
        val map = hashMapOf(pos to 0)
        var steps = 0
        for (i in input) {
            repeat(i.extractNumbers().toInt()) {
                when (i.first()) {
                    'U' -> pos += Point(0, 1)
                    'R' -> pos += Point(1, 0)
                    'D' -> pos += Point(0, -1)
                    'L' -> pos += Point(-1, 0)
                }
                steps++
                if(!map.containsKey(pos))
                    map[pos] = steps
            }
        }
        return map
    }
}