package solutions.y2023

import Solution
import utils.Utils.rl
import utils.grid.Grid
import utils.movement.Direction
import utils.point.Point
import java.io.File
import java.util.*

class Day10 : Solution(2023) {

    override fun solvePart1(input: File): Any {
        val grid = Grid.of(input.rl())
        val start = grid['S']!!
        val stack = Stack<Pair<Point, Int>>()
        val visited = mutableSetOf<Point>()
        stack.push(start to 0)
        var max = 0
        while (stack.isNotEmpty()) {
            val (point, steps) = stack.pop()
            max = maxOf(max, steps)
            for (neighbor in point.getCardinalNeighbors()) {
                if (grid[neighbor] == null || neighbor in stack.map { it.first }) continue
                val dx = neighbor.x - point.x
                val dy = neighbor.y - point.y
                if (isConnecting(grid[point]!!, grid[neighbor]!!, dx, dy) && neighbor !in visited) {
                    visited.add(neighbor)
                    stack.push(neighbor to steps + 1)
                }
            }
        }
        return max / 2 + 1
    }

    private fun isConnecting(cur: Char, nei: Char, dx: Int, dy: Int): Boolean {
        val up = dx == -1 && dy == 0
        val down = dx == 1 && dy == 0
        val left = dx == 0 && dy == -1
        val right = dx == 0 && dy == 1
        return when (cur) {
            'S' -> (nei in "|7F" && up) || (nei in "|LJ" && down) || (nei in "-J7" && right) || (nei in "-FL" && left)
            '|' -> (nei in "LJ" && down) || (nei in "F7" && up) || (nei == '|' && dy == 0)
            '-' -> (nei in "FL" && left) || (nei in "J7" && right) || (nei == '-' && dx == 0)
            'J' -> (nei in "7F|" && up) || (nei in "LF-" && left)
            'F' -> (nei in "J|L" && down) || (nei in "7-J" && right)
            '7' -> (nei in "FL-" && left) || (nei in "J|L" && down)
            'L' -> (nei in "7F|" && up) || (nei in "J-7" && right)
            else -> false
        }
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl())
        val start = grid.getPointsWithValue('S').first()
        val stack = Stack<Pair<Point, Int>>()
        val path = mutableSetOf(start)
        stack.push(start to 0)
        while (stack.isNotEmpty()) {
            val (point, steps) = stack.pop()
            for (neighbor in point.getCardinalNeighbors()) {
                if (grid[neighbor] == null || neighbor in stack.map { it.first }) continue
                val (dx, dy) = neighbor.x - point.x to neighbor.y - point.y
                if (isConnecting(grid[point]!!, grid[neighbor]!!, dx, dy) && neighbor !in path) {
                    path.add(neighbor)
                    stack.push(neighbor to steps + 1)
                }
            }
        }
        var area = 0
        for (i in 0 until grid.rows) {
            for (j in 0 until grid.columns) {
                val point = Point(i, j)
                if (point in path) continue
                var windings = 0
                for (k in j downTo 0) {
                    val test = Point(i, k)
                    if (grid[test]!! in (if (grid[start.gridPlus(Direction.NORTH)]!! in "|7F") "|JLS" else "|JL") && test in path) windings++
                }
                if (windings % 2 == 1) area++
            }
        }
        return area
    }
}
