package solutions.y2023

import Solution
import arrow.core.Tuple4
import utils.Utils.asInt
import utils.Utils.p
import utils.Utils.rl
import utils.grid.Grid
import utils.movement.Direction
import utils.point.Point
import java.io.File
import java.util.*

class Day17 : Solution(2023) {

    override fun solvePart1(input: File): Any {
        val grid = Grid.of(input.rl())
        val queue = PriorityQueue<Tuple4<Point, Direction?, Int, Int>>(compareBy { it.fourth })
        val visited = hashSetOf<Triple<Point, Direction?, Int>>()
        queue.add(Tuple4(Point(0, 0), null, 0, 0))
        val end = grid.maxX p grid.maxY

        while (queue.isNotEmpty()) {
            val (cur, dir, repeat, hl) = queue.poll()
            if (Triple(cur, dir, repeat) in visited) continue
            if (cur == end) return hl
            visited.add(Triple(cur, dir, repeat))
            if (repeat < 3 && dir != null) {
                val next = cur.gridPlus(dir)
                if (grid[next] != null)
                    queue.add(Tuple4(next, dir, repeat + 1, hl + grid[next]!!.asInt()))
            }
            for (nd in Direction.entries) {
                if (nd == dir || nd == (dir?.opposite() ?: dir)) continue
                val next = cur.gridPlus(nd)
                if (grid[next] != null)
                    queue.add(Tuple4(next, nd, 1, hl + grid[next]!!.asInt()))
            }
        }
        return ":( no path found :("
    }

    override fun solvePart2(input: File): Any {
        val grid = Grid.of(input.rl())
        val queue = PriorityQueue<Tuple4<Point, Direction?, Int, Int>>(compareBy { it.fourth })
        val visited = hashSetOf<Triple<Point, Direction?, Int>>()
        queue.add(Tuple4(Point(0, 0), null, 0, 0))
        val end = grid.maxX p grid.maxY

        while (queue.isNotEmpty()) {
            val (cur, dir, repeat, hl) = queue.poll()
            if (Triple(cur, dir, repeat) in visited) continue
            if (cur == end && repeat >= 4) return hl
            visited.add(Triple(cur, dir, repeat))
            if (repeat < 10 && dir != null) {
                val next = cur.gridPlus(dir)
                if (grid[next] != null)
                    queue.add(Tuple4(next, dir, repeat + 1, hl + grid[next]!!.asInt()))
            }
            if (repeat >= 4 || dir == null)
                for (nd in Direction.entries) {
                    if (nd == dir || nd == (dir?.opposite() ?: dir)) continue
                    val next = cur.gridPlus(nd)
                    if (grid[next] != null)
                        queue.add(Tuple4(next, nd, 1, hl + grid[next]!!.asInt()))
                }
        }
        return ":( no path found :("
    }
}