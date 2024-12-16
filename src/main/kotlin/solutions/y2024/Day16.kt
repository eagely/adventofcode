package solutions.y2024

import Solution
import utils.chargrid
import utils.movement.Direction
import utils.point.Point
import java.io.File
import java.util.*
import kotlin.collections.set

class Day16 : Solution(2024) {

    override fun solvePart1(input: File): Any {
        val grid = input.chargrid()
        val queue = PriorityQueue<Triple<Point, Direction, Int>>(compareBy { it.third })
        val start = grid['S']!!
        queue.add(Triple(start, Direction.EAST, 0))
        val end = grid['E']!!

        val seen = HashMap<Pair<Point, Direction>, Int>()
        while (queue.isNotEmpty()) {
            val (p, d, s) = queue.poll()

            if (p == end) return s

            if (seen[p to d] != null && seen[p to d]!! < s) continue
            seen[p to d] = s
            if (grid[p + d] != '#')
                queue.add(Triple(p + d, d, s + 1))
            queue.add(Triple(p, d + Direction.EAST, s + 1000))
            queue.add(Triple(p, d + Direction.WEST, s + 1000))
        }
        return 0
    }

    // #71 global
    override fun solvePart2(input: File): Any {
        val grid = input.chargrid()
        val queue = PriorityQueue<Triple<List<Point>, Direction, Int>>(compareBy { it.third })
        val start = grid['S']!!
        queue.add(Triple(listOf(start), Direction.EAST, 0))
        val end = grid['E']!!

        var min = Int.MAX_VALUE
        val best = HashSet<Point>()
        val seen = HashMap<Pair<Point, Direction>, Int>()
        while (queue.isNotEmpty()) {
            val (p, d, s) = queue.poll()

            if (p.last() == end) {
                if (s <= min) min = s
                else return best.size
                best.addAll(p)
            }

            if (seen[p.last() to d] != null && seen[p.last() to d]!! < s) continue
            seen[p.last() to d] = s
            if (grid[p.last() + d] != '#')
                queue.add(Triple(p + (p.last() + d), d, s + 1))
            queue.add(Triple(p, d + Direction.EAST, s + 1000))
            queue.add(Triple(p, d + Direction.WEST, s + 1000))
        }
        return 0
    }
}