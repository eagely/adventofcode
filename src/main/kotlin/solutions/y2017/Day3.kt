package solutions.y2017

import Solution
import java.io.File
import utils.*
import utils.grid.Grid
import utils.point.Point
import kotlin.math.ceil

class Day3 : Solution(2017) {

    override fun solvePart1(input: File): Any {
        val goal = input.text.toInt()
        val order = cardinalDirections.cyclicIterator()
        var pos = Point(0, 0)
        var v = 1
        var c = 0
        while ((v + ceil(c / 2.0).toInt()) < goal) {
            val mul = ceil(c / 2.0).toInt()
            c++
            pos += (order.next() * mul)
            v += mul
        }
        return (pos + (order.next() * (goal - v))).manhattanDistance(Point(0, 0))
    }

    override fun solvePart2(input: File): Any {
        val goal = input.text.toInt()
        val order = cardinalDirections.cyclicIterator()
        var pos = Point(0, 0)
        var c = 0
        val grid = Grid<Int>().apply { this[Point(0, 0)] = 1}
        while (true) {
            c++
            val dir = order.next()
            repeat(ceil(c / 2.0).toInt()) {
                pos += dir
                grid[pos] = grid.getNeighbors(pos).sumOf { it ?: 0 }
                grid[pos]!!.let { if (it > goal) return it }
            }
        }
    }
}