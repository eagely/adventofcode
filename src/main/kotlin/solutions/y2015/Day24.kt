package solutions.y2015

import Solution
import java.io.File
import utils.*
import kotlin.math.min

class Day24 : Solution(2015) {

    override fun solvePart1(input: File) = bfs(input.ints, 3)

    override fun solvePart2(input: File) = bfs(input.ints, 4)

    private fun bfs(lines: List<Int>, amt: Int): Long {
        val req = lines.sum() / amt
        val queue = arrayDequeOf<List<Int>>(listOf())
        val cache = hashSetOf<List<Int>>()
        var min = Long.MAX_VALUE
        var minsize = Int.MAX_VALUE

        while (queue.isNotEmpty()) {
            val cur = queue.removeLast()
            val sum = cur.sum()
            if (!cache.add(cur.sorted()) || cur.size > minsize || sum > req) continue

            if (sum == req && cur.size <= minsize) {
                min = min(min, cur.map { it.toLong() }.product())
                minsize = cur.size
                continue
            }

            queue.addAll(lines.filter { it !in cur && sum + it <= req }.map { cur + it })
        }

        return min
    }
}