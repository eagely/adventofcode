package solutions.y2019

import Solution
import utils.chargrid
import utils.gcd
import utils.point.Point
import java.io.File
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

class Day10 : Solution(2019) {

    override fun solvePart1(input: File) = parse(input).getStation().first

    override fun solvePart2(input: File): Any {
        val asteroids = parse(input)
        val station = asteroids.getStation().second
        var i = 0
        while (true) {
            asteroids.filter { !it.isBlocked(station, asteroids) }.sortedBy { asteroid ->
                val angle = atan2((asteroid.x - station.x).toDouble(), -(asteroid.y - station.y).toDouble())
                if (angle < 0) angle + 2 * PI else angle
            }.forEach {
                asteroids.remove(it)
                i++
                if (i >= 200) return it.let { it.x * 100 + it.y }
            }
        }
    }

    private fun parse(input: File) = input.chargrid().filter { it == '#' }.data.keys.map { it.invert() }.toMutableList()

    private fun Point.isBlocked(station: Point, asteroids: List<Point>): Boolean {
        if (this == station) return true
        val (dx, dy) = this - station
        val g = abs(gcd(dx, dy))
        val reduced = Point(dx / g, dy / g)
        return asteroids.any { asteroid ->
            asteroid != station && asteroid != this && run {
                val (ndx, ndy) = asteroid - station
                val ng = abs(gcd(ndx, ndy))
                reduced == Point(ndx / ng, ndy / ng) && manhattanDistance(station) > asteroid.manhattanDistance(station)
            }
        }
    }

    private fun List<Point>.getStation() = map { station ->
        filter { it != station }.map { asteroid ->
            val dx = asteroid.x - station.x
            val dy = asteroid.y - station.y
            val g = abs(gcd(dx, dy))
            Point(dx / g, dy / g)
        }.distinct().size to station
    }.maxBy { it.first }
}