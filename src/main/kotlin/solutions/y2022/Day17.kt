package solutions.y2022

import Solution
import utils.grid.Grid
import utils.point.Point
import java.io.File

class Day17 : Solution(2022) {
    override fun solvePart1(input: File): Any {
        val path = input.readText().trim()
        val grid = Grid<Char>(1, 7)
        var i = 0
        var resting: Boolean
        var cur: Set<Point>
        for (j in 1..<2023) {
            cur = getBlock(j, (grid.getHighestOfValue('#') ?: Point(0, 0)).y)
            grid[cur] = '#'
            resting = false
            var moveDown = false
            while (!resting) {
                if (moveDown && grid.canMove(cur, 0, -1) && cur.all { it.y > 1 }) {
                    cur = grid.movePointsBy(cur, 0, -1)
                } else if (!moveDown) {
                    cur = when (path[i]) {
                        '<' -> {
                            if (cur.any { it.x == 0 }) grid.movePointsBy(cur, 0, 0)
                            else if (grid.canMove(cur, -1, 0)) grid.movePointsBy(cur, -1, 0)
                            else grid.movePointsBy(cur, 0, 0)
                        }
                        '>' -> {
                            if (cur.any { it.x == 6 }) grid.movePointsBy(cur, 0, 0)
                            else if (grid.canMove(cur, 1, 0)) grid.movePointsBy(cur, 1, 0)
                            else grid.movePointsBy(cur, 0, 0)
                        }
                        else -> throw Exception("Input goofed")
                    }
                    if (++i >= path.length) i = 0
                } else {
                    resting = true
                }
                moveDown = !moveDown
            }
        }
        return grid.getHighestOfValue('#')!!.y
    }


    override fun solvePart2(input: File): Any {
        val path = input.readText().trim()
        val grid = Grid<Char>(1, 7)
        var i = 0
        var resting: Boolean
        var cur: Set<Point>
        val absHeights = mutableListOf<Int>()
        for (j in 1..<5000) {
            cur = getBlock(j, (grid.getHighestOfValue('#') ?: Point(0, 0)).y)
            grid[cur] = '#'
            resting = false
            var moveDown = false
            while (!resting) {
                if (moveDown && grid.canMove(cur, 0, -1) && cur.all { it.y > 1 }) {
                    cur = grid.movePointsBy(cur, 0, -1)
                } else if (!moveDown) {
                    cur = when (path[i]) {
                        '<' -> {
                            if (cur.any { it.x == 0 }) grid.movePointsBy(cur, 0, 0)
                            else if (grid.canMove(cur, -1, 0)) grid.movePointsBy(cur, -1, 0)
                            else grid.movePointsBy(cur, 0, 0)
                        }
                        '>' -> {
                            if (cur.any { it.x == 6 }) grid.movePointsBy(cur, 0, 0)
                            else if (grid.canMove(cur, 1, 0)) grid.movePointsBy(cur, 1, 0)
                            else grid.movePointsBy(cur, 0, 0)
                        }
                        else -> throw Exception("Input goofed")
                    }
                    if (++i >= path.length) i = 0
                } else {
                    resting = true
                }
                moveDown = !moveDown
            }
            absHeights += grid.getHighestOfValue('#')!!.y
        }
        // I pasted a height difference map into a text editor and manually found the required values, might make it automatic later
        val patternStartIndex = 1039
        val patternLength = 1740

        val heightDifferencePerCycle = absHeights[patternStartIndex + patternLength] - absHeights[patternStartIndex]

        val numberOfCompleteCycles = (1000000000000 - patternStartIndex) / patternLength

        val indexForRemainingRocks = ((1000000000000 - patternStartIndex - 1) % patternLength).toInt() + patternStartIndex

        val totalHeight = absHeights[patternStartIndex] +
                (numberOfCompleteCycles * heightDifferencePerCycle) +
                (absHeights[indexForRemainingRocks] - absHeights[patternStartIndex])

        println("The tower will be $totalHeight units tall after 1000000000000 rocks have stopped falling.")
        return 0
    }

    private fun getBlock(index: Int, height: Int): Set<Point> {
        return when (index % 5) {
            0 -> setOf(Point(2, height+5), Point(3, height+5), Point(2, height+4), Point(3, height+4))
            1 -> setOf(Point(2, height+4), Point(3, height+4), Point(4, height+4), Point(5, height+4))
            2 -> setOf(Point(2, height+5), Point(3, height+5), Point(4, height+5), Point(3, height+4), Point(3, height+6))
            3 -> setOf(Point(2, height+4), Point(3, height+4), Point(4, height+4), Point(4, height+5), Point(4, height+6))
            4 -> setOf(Point(2, height+4), Point(2, height+5), Point(2, height+6), Point(2, height+7))
            else -> throw Exception("This is mathematically not possible")
        }
    }
    private fun Grid<Char>.getHighestOfValue(value: Char): Point? = data.filterValues { it == value }.keys.maxByOrNull { it.y }
}