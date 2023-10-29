
package solutions.y2022

import Solution
import utils.point.Point
import java.io.File
import kotlin.math.abs
import java.util.*

class Day12 : Solution(2022) {

    data class Location(val char: Char, val point: Point) {
        val neighbors = mutableListOf<Point>()

        fun elevation(): Char = when (char) {
            'S' -> 'a'
            'E' -> 'z'
            else -> char
        }
    }

    override fun solvePart1(input: File): String {
        val grid = parseInput(input.readLines())
        val start = grid.find { it.char == 'S' }!!
        val end = grid.find { it.char == 'E' }!!

        val distanceToEnd = aStar(grid, start, end)
        return distanceToEnd.toString()
    }

    override fun solvePart2(input: File): Int {
        val grid = parseInput(input.readLines())
        val end = grid.find { it.char == 'E' }!!

        val startPoints = grid.filter {
            it.char == 'a' && it.neighbors.any { neighborPoint ->
                grid.first { loc -> loc.point == neighborPoint }.char == 'b'
            }
        }

        return startPoints.minOfOrNull { startLocation ->
            aStar(grid, startLocation, end)
        } ?: -1
    }

    private fun aStar(grid: List<Location>, start: Location, end: Location): Int {
        val gScores = mutableMapOf<Location, Int>()
        val fScores = mutableMapOf<Location, Int>()
        val openSet = PriorityQueue<Location>(compareBy { fScores[it] ?: Int.MAX_VALUE })

        gScores[start] = 0
        fScores[start] = heuristic(start, end)
        openSet.add(start)

        while (openSet.isNotEmpty()) {
            val current = openSet.poll()

            if (current == end) {
                return gScores[current]!!
            }

            for (neighborPoint in current.neighbors) {
                val neighbor = grid.first { it.point == neighborPoint }
                if (neighbor.elevation() <= current.elevation() + 1) {
                    val tentativeGScore = gScores[current]!! + 1
                    val previousGScore = gScores.getOrDefault(neighbor, Int.MAX_VALUE)

                    if (tentativeGScore < previousGScore) {
                        gScores[neighbor] = tentativeGScore
                        fScores[neighbor] = tentativeGScore + heuristic(neighbor, end)
                        if (openSet.none { it == neighbor }) {
                            openSet.add(neighbor)
                        }
                    }
                }
            }
        }
        return Int.MAX_VALUE
    }

    private fun heuristic(node: Location, target: Location): Int {
        return abs(node.point.x - target.point.x) + abs(node.point.y - target.point.y)
    }

    private fun parseInput(input: List<String>): List<Location> {
        val locations = mutableListOf<Location>()
        for (y in input.indices) {
            for (x in input[y].indices) {
                val char = input[y][x]
                val location = Location(char, Point(x, y))
                locations.add(location)
            }
        }

        for (location in locations) {
            for (neighbor in location.point.getCardinalNeighbors()) {
                if (locations.map { it.point }.contains(neighbor)) {
                    location.neighbors.add(neighbor)
                }
            }
        }

        return locations
    }
}

