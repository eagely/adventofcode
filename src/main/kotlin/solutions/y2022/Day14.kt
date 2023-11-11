package solutions.y2022

import Solution
import utils.grid.Grid
import utils.point.Point
import java.io.File

class Day14 : Solution(2022) {

    private val origin = Point(500, 0)

    override fun solvePart1(input: File): Int {
        val cave = parse(input.readLines())

        var sandCount = 0
        var currentPos: Point
        var nextPos: Point

        outer@ while (true) {
            currentPos = origin.copy()
            nextPos = currentPos.u

            inner@ while (true) {
                if (nextPos.y > cave.maxY) break@outer

                if (cave.getOrDefault(nextPos, '.') == '.') {
                    currentPos = nextPos
                    nextPos = currentPos.u
                } else if (cave.getOrDefault(currentPos.ul, '.') == '.') {
                    currentPos = currentPos.ul
                    nextPos = currentPos.u
                } else if (cave.getOrDefault(currentPos.ur, '.') == '.') {
                    currentPos = currentPos.ur
                    nextPos = currentPos.u
                } else break@inner
            }

            cave[currentPos] = 'o'
            sandCount++
        }
        return sandCount
    }

    override fun solvePart2(input: File): Int {
        val cave = parse(input.readLines())

        var sandCount = 0
        var currentPos: Point
        var nextPos: Point
        val yLimit = cave.maxY + 2

        outer@ while (true) {
            currentPos = origin.copy()
            nextPos = currentPos.u

            inner@ while (true) {
                if (cave[origin] == 'o') break@outer

                if (nextPos.y == yLimit) {
                    break@inner
                }
                if (cave.getOrDefault(nextPos, '.') == '.') {
                    currentPos = nextPos
                    nextPos = currentPos.u
                } else if (cave.getOrDefault(currentPos.ul, '.') == '.') {
                    currentPos = currentPos.ul
                    nextPos = currentPos.u
                } else if (cave.getOrDefault(currentPos.ur, '.') == '.') {
                    currentPos = currentPos.ur
                    nextPos = currentPos.u
                } else break@inner
            }
            cave[currentPos] = 'o'
            sandCount++
        }
        return sandCount
    }

    private fun parse(lines: List<String>): Grid<Char> {
        val rocks = lines.map { line ->
            line.split(" -> ")
                .map { it.split(",").let { pos -> Point(pos[0].toInt(), pos[1].toInt()) } }
        }

        val grid = Grid<Char>().apply {
            rocks.forEach { points ->
                points.windowed(2).forEach { (start, end) ->
                    if (start.x == end.x) {
                        val yRange = if (start.y <= end.y) start.y..end.y else end.y..start.y
                        yRange.forEach { y -> this[Point(start.x, y)] = '#' }
                    } else if (start.y == end.y) {
                        val xRange = if (start.x <= end.x) start.x..end.x else end.x..start.x
                        xRange.forEach { x -> this[Point(x, start.y)] = '#' }
                    }
                }
            }
        }
        return grid
    }
}