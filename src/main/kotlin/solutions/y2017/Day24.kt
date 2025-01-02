package solutions.y2017

import Solution
import utils.allEquals
import utils.annotations.NoTest
import utils.lines
import java.io.File

class Day24 : Solution(2017) {

    private data class Component(val id: Int, val connectors: List<Int>)

    @NoTest
    override fun solvePart1(input: File): Any {
        buildBridges(input)
        return bridges.maxOf { b -> b.sumOf { it.connectors.sum() } }
    }

    @NoTest
    override fun solvePart2(input: File): Any {
        val ms = bridges.maxOf { it.size }
        return bridges.filter { it.size == ms }.maxOf { b -> b.sumOf { it.connectors.sum() } }
    }

    private val bridges = mutableSetOf<List<Component>>()
    private fun buildBridges(input: File) {
        val ports = input.lines.mapIndexed { i, line -> line.split('/').map { it.toInt() }.let { Component(i, it) } }

        val q = java.util.ArrayDeque<List<Component>>()
        q.addAll(ports.filter { it.connectors.first() == 0 }.map { listOf(it) })

        while (q.isNotEmpty()) {
            var seen = q.removeFirst()
            val cur = seen.last()
            val con = cur.connectors.last()

            val immediate = ports.filter { c ->
                seen.none { it.id == c.id } && c.connectors.allEquals(con)
            }
            seen += immediate

            val adj = ports.filter { c ->
                seen.none { it.id == c.id } && c.connectors.any { it == con }
            }
            if (adj.isEmpty()) bridges.add(seen)
            q.addAll(adj.map {
                val (id, c) = it
                val (f, s) = c
                if (f == con) seen + it
                else seen + Component(id, listOf(s, f))
            })
        }
    }
}