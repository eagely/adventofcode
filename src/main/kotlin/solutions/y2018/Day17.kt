package solutions.y2018

import Solution
import utils.extractNumbersSeparated
import utils.lines
import utils.point.Point
import java.io.File

class Day17 : Solution(2018) {

    private lateinit var grid: Array<CharArray>

    override fun solvePart1(input: File): Any {
        val minX = parse(input)
        flow()
        return grid.drop(minX).sumOf { row -> row.count { it == '|' || it == '~' } }
    }

    override fun solvePart2(input: File): Any {
        val minX = parse(input)
        flow()
        return grid.drop(minX).sumOf { row -> row.count { it == '~' } }
    }

    private fun parse(input: File): Int {
        val clay = input.lines.flatMap { line ->
            line.extractNumbersSeparated().let { p ->
                (p[1]..p[2]).map {
                    if (line[0] == 'y') Point(p[0], it)
                    else Point(it, p[0])
                }
            }
        }

        val maxX = clay.maxOf { it.x }
        val maxY = clay.maxOf { it.y }

        grid = (0..maxX).map {
            CharArray(maxY + 2) { '.' }
        }.toTypedArray()

        clay.forEach {
            grid[it] = '#'
        }

        return clay.minOf { it.x }
    }

    private fun flow() {
        val stack = ArrayDeque<Point>()
        stack.add(Point(0, 500))

        while (stack.isNotEmpty()) {
            val cur = stack.removeLast()
            if (cur.down !in grid) continue

            when (grid[cur.down]) {
                '.' -> {
                    grid[cur.down] = '|'
                    stack.add(cur)
                    stack.add(cur.down)
                }

                '#', '~' -> {
                    if (cur.right in grid && grid[cur.right] == '.') {
                        grid[cur.right] = '|'
                        stack.add(cur)
                        stack.add(cur.right)
                    }
                    if (cur.left in grid && grid[cur.left] == '.') {
                        grid[cur.left] = '|'
                        stack.add(cur)
                        stack.add(cur.left)
                    }
                }
            }

            if (hasWall(cur, Point::right) && hasWall(cur, Point::left)) {
                fillUntilWall(cur, Point::right)
                fillUntilWall(cur, Point::left)
            }
        }
    }

    private fun hasWall(source: Point, nextPoint: (Point) -> Point): Boolean {
        var point = source
        while (point in grid) {
            when (grid[point]) {
                '#' -> return true
                '.' -> return false
                else -> point = nextPoint(point)
            }
        }
        return false
    }

    private fun fillUntilWall(source: Point, nextPoint: (Point) -> Point) {
        var point = source
        while (grid[point] != '#') {
            grid[point] = '~'
            point = nextPoint(point)
        }
    }

    private operator fun Array<CharArray>.get(point: Point): Char = this[point.x][point.y]

    private operator fun Array<CharArray>.set(point: Point, value: Char) {
        this[point.x][point.y] = value
    }

    private operator fun Array<CharArray>.contains(point: Point): Boolean = point.x >= 0 && point.x < this.size && point.y >= 0 && point.y < this[0].size
}