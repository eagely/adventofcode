package solutions.y2017

import Solution
import java.io.File
import utils.*

class Day12 : Solution(2017) {

    override fun solvePart1(input: File): Any {
        val map = input.lines.associate { it.extractNumbersSeparated().let { it.first() to it.drop(1) } }
        val queue = ArrayDeque<Int>()
        var c = 0
        for (k in map.keys) {
            val visited = HashSet<Int>()
            queue.add(k)
            while(queue.isNotEmpty()) {
                val cur = queue.removeFirst()
                visited.add(cur)
                if (0 in map[cur]!!) {
                    c++
                    break
                } else {
                    queue.addAll(map[cur]!!.filter { it !in visited })
                }
            }
            queue.clear()
        }
        return c
    }

    override fun solvePart2(input: File): Any {
        val map = input.lines.associate { it.extractNumbersSeparated().let { it.first() to it.drop(1) } }
        val groups = HashSet<Set<Int>>()
        val queue = ArrayDeque<Int>()
        for (k in map.keys) {
            val visited = HashSet<Int>()
            queue.add(k)
            while(queue.isNotEmpty()) {
                val cur = queue.removeFirst()
                visited.add(cur)
                queue.addAll(map[cur]!!.filter { it !in visited })
            }
            groups += visited
            queue.clear()
        }
        return groups.size
    }
}