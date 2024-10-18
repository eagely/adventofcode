package solutions.y2018

import Solution
import utils.movement.Direction
import utils.point.Point
import utils.text
import java.io.File
import kotlin.math.min

class Day20 : Solution(2018) {

    override fun solvePart1(input: File) = getRooms(input).maxOf { it.value }

    override fun solvePart2(input: File) = getRooms(input).count { it.value >= 1000 }

    private fun getRooms(input: File): MutableMap<Point, Int> {
        var pos = Point(0, 0)
        val rooms = mutableMapOf(pos to 0)
        val checkpoint = ArrayDeque<Point>()
        input.text.forEach { c ->
            when (c) {
                '(' -> checkpoint.add(pos)
                ')' -> pos = checkpoint.removeLast()
                '|' -> pos = checkpoint.last()
                'N', 'E', 'S', 'W' -> {
                    val next = pos + Direction.of(c)
                    rooms[next] = min(rooms[next] ?: (rooms[pos]!! + 1), rooms[pos]!! + 1)
                    pos = next
                }
            }
        }
        return rooms
    }
}
