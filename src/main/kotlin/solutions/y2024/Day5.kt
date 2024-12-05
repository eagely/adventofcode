package solutions.y2024

import Solution
import utils.sdanl
import java.io.File
import java.util.*

class Day5 : Solution(2024) {

    override fun solvePart1(input: File): Any {
        val (order, pages) = parse(input)

        return pages.filter { it.isOrdered(order) }.sumOf { it[it.size / 2] }
    }

    override fun solvePart2(input: File): Any {
        val (order, pages) = parse(input)
        var res = 0

        for (page in pages.filterNot { it.isOrdered(order) }) {
            val deps = page.associateWith { num -> order[num]?.filter { it in page }?.toMutableSet() ?: mutableSetOf() }
                .toMutableMap()
            val out = mutableListOf<Int>()
            val pq = PriorityQueue<Int>(compareBy { deps[it]?.size ?: 0 })
            pq.addAll(page)
            while (pq.isNotEmpty()) {
                val cur = pq.remove()
                out += cur
                deps.values.forEach { it.remove(cur) }
            }
            res += out[out.size / 2]
        }

        return res
    }

    private fun List<Int>.isOrdered(order: Map<Int, List<Int>>): Boolean {
        val cache = HashSet<Int>()
        return all { c ->
            cache.add(c)
            order[c] == null || order[c]!!.none { it !in cache && it in this }
        }
    }

    private fun parse(input: File) = input.sdanl().let {
        it.first().map { it.split("|") }.groupBy({ it.last().toInt() }, { it.first().toInt() }) to it.last()
            .map { it.split(",").map { it.toInt() } }.toSet()
    }
}