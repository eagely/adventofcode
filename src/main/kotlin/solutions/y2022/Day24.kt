package solutions.y2022

import Solution
import utils.Utils.p
import utils.Utils.rl
import utils.grid.Grid
import utils.movement.Direction
import utils.movement.Direction.*
import utils.point.Point
import java.io.File
import java.util.*

class Day24 : Solution(2022) {
    override fun solvePart1(input: File): Any {
        val grid = Grid.of(input.rl().drop(1).dropLast(1).map { it.drop(1).dropLast(1) })
        val start = -1 p 0

        val goal = Point(grid.rows, grid.columns - 1)
        return bfs(start, goal, grid)
    }

    private fun bfs(start: Point, goal: Point, grid: Grid<Char>, startTime: Int = 0): Int {
        val blizzards = hashMapOf(
            NORTH to grid.getPointsWithValue('^'),
            EAST to grid.getPointsWithValue('>'),
            SOUTH to grid.getPointsWithValue('v'),
            WEST to grid.getPointsWithValue('<')
        )
        val queue = LinkedList<Pair<Point, Int>>().apply { add(start to startTime) }
        val memo = hashSetOf<Pair<Point, Int>>()
        val lcm = lcm(grid.rows, grid.columns)
        while (queue.isNotEmpty()) {
            var (cur, time) = queue.poll()
            if (cur in goal.getCardinalNeighbors()) return time + 1
            if (memo.contains(cur to (time % lcm))) continue
            else memo.add(cur to time % lcm)
            time++
            for (new in cur.getCardinalNeighbors() + setOf(cur)) {
                if ((new.x < 0 || new.y < 0 || new.x >= grid.rows || new.y >= grid.columns) && new != start) continue
                if (isBlizzard(new, time, blizzards, grid)) continue
                queue.add(new to time)
            }
        }
        return 0
    }

    private fun isBlizzard(
        point: Point,
        time: Int,
        blizzards: HashMap<Direction, Set<Point>>,
        grid: Grid<Char>
    ): Boolean {
        blizzards.forEach { (dir, points) ->
            val checkPoint = when (dir) {
                NORTH -> Point((point.x + time + grid.rows * 50) % grid.rows, point.y)
                EAST -> Point(point.x, (point.y - time + grid.columns * 50) % grid.columns)
                SOUTH -> Point((point.x - time + grid.rows * 50) % grid.rows, point.y)
                WEST -> Point(point.x, (point.y + time + grid.columns * 50) % grid.columns)
            }
            if (points.contains(checkPoint)) return true
        }
        return false
    }

    private fun gcd(a: Int, b: Int): Int {
        if (b == 0) return a
        return gcd(b, a % b)
    }

    private fun lcm(a: Int, b: Int): Int {
        return a * b / gcd(a, b)
    }


    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl().drop(1).dropLast(1).map { it.drop(1).dropLast(1) })
        val start = -1 p 0

        val goal = Point(grid.rows, grid.columns - 1)

        return bfs(start, goal, grid, bfs(goal, start, grid, bfs(start, goal, grid)))
    }
}