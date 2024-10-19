package solutions.y2018

import Solution
import utils.*
import utils.point.Point
import java.io.File
import java.util.*
import kotlin.math.abs

class Day22 : Solution(2018) {

    override fun solvePart1(input: File): Any {
        val (depth, target) = parse(input)
        return getGrid(depth, target).sumOf { it.sum() }
    }

    private data class State(var tool: Int, var pos: Point, var cost: Int)

    override fun solvePart2(input: File): Any {
        val (depth, target) = parse(input)
        val grid = getGrid(depth, target, 100)
        val pq = PriorityQueue<State>(compareBy { it.cost + abs(it.pos.x - target.x) + abs(it.pos.y - target.y) })
        pq.add(State(1, Point(0, 0), 0))
        val memo = HashMap<Pair<Point, Int>, Int>()

        while (pq.isNotEmpty()) {
            val (e, p, c) = pq.remove()

            if (e == grid[p]) die()

            val key = p to e
            if (key in memo && memo[key]!! <= c) continue
            memo[key] = c

            if (p == target && e == 1) {
                return c
            }

            val tools = (setOf(0, 1, 2) - grid[p])
            pq.addAll(tools.map {
                State(it, p, c + 7)
            })
            for (n in p.getCardinalNeighbors().filter { it in grid && it.x >= 0 && it.y >= 0 }) {
                if (e != grid[n]) {
                    pq.add(State(e, n, c + 1))
                }
            }
        }
        return "no path"
    }

    private fun getGrid(depth: Int, target: Point, extra: Int = 0): Array<IntArray> {
        val calculator = GeologicCalculator(depth, target)
        return Array(target.x + extra + 1) { x ->
            IntArray(target.y + extra + 1) { y ->
                calculator.getErosion(Point(x, y)) % 3
            }
        }
    }

    private class GeologicCalculator(private val depth: Int, target: Point) {
        private val geoIndex = hashMapOf(target to 0)

        fun getGeologicIndex(point: Point): Int {
            geoIndex[point]?.let { return it }
            geoIndex[point] = when {
                point.y == 0 -> point.x * 16807
                point.x == 0 -> point.y * 48271
                else -> (getErosion(Point(point.x - 1, point.y)) * getErosion(Point(point.x, point.y - 1))) % 20183
            }
            return geoIndex[point]!!
        }

        fun getErosion(point: Point) = (getGeologicIndex(point) + depth) % 20183
    }


    private fun parse(input: File): Pair<Int, Point> {
        val lines = input.lines
        return lines.first().extractNumbers().toInt() to Point.of(lines.last().after(' '))
    }
}