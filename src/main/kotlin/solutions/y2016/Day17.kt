package solutions.y2016

import Solution
import utils.md5
import utils.p
import utils.text
import java.io.File
import java.util.*

class Day17 : Solution(2016) {

    override fun solvePart1(input: File): Any {
        val salt = input.text
        val queue = ArrayDeque(listOf(0 p 0 to ""))
        while (queue.isNotEmpty()) {
            val (pos, path) = queue.removeFirst()
            if (pos == 3 p 3) return path
            val hash = "$salt$path".md5()
            for (i in 0..3) if (hash[i] in "bcdef") {
                val next = pos + listOf(-1 p 0, 1 p 0, 0 p -1, 0 p 1)[i]
                if (next.x in 0..3 && next.y in 0..3) queue.add(next to path + "UDLR"[i])
            }
        }
        return "No path found"
    }

    override fun solvePart2(input: File): Any {
        val salt = input.text
        val stack = ArrayDeque(listOf(0 p 0 to ""))
        var max = 0
        while (stack.isNotEmpty()) {
            val (pos, path) = stack.removeFirst()
            if (pos == 3 p 3) {
                max = maxOf(max, path.length)
                continue
            }
            val hash = "$salt$path".md5()
            for (i in 0..3)
                if (hash[i] in "bcdef") {
                    val next = pos + listOf(-1 p 0, 1 p 0, 0 p -1, 0 p 1)[i]
                    if (next.x in 0..3 && next.y in 0..3) stack.add(next to path + "UDLR"[i])
                }
        }
        return max
    }
}