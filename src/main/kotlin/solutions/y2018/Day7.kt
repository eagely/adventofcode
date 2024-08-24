package solutions.y2018

import Solution
import java.io.File
import utils.*
import utils.annotations.NoTest

class Day7 : Solution(2018) {

    @NoTest
    override fun solvePart1(input: File): Any {
        val deps = parseDependencies(input.lines)
        var order = ""
        while (order.length != 26) {
            order += ('A'..'Z').filter { step -> step !in order && deps[step]?.let { dep -> dep.all { it in order } } ?: true }.minOf { it }
        }
        return order
    }

    data class Task(val designator: Char, val time: Int)

    @NoTest
    override fun solvePart2(input: File): Any {
        val deps = parseDependencies(input.lines)
        var seconds = -1
        var workers = mutableSetOf<Task>()
        val completed = mutableSetOf<Char>()
        while (completed.size != 26) {
            seconds++
            for (task in workers)
                if (seconds >= task.time)
                    completed += task.designator
            workers = workers.filter { it.designator !in completed }.toMutableSet()
            workers.addAll(('A'..'Z').filter { step -> step !in completed && deps[step]?.let { dep -> dep.all { it in completed } } ?: true && workers.none { task -> task.designator == step } }.sorted().take(5 - workers.size).map { Task(it, it.code - 4 + seconds) })
        }
        return seconds
    }

    private fun parseDependencies(input: List<String>) = input.map { line -> line.split().let { it[1].toChar() to it[7].toChar() } }.groupBy({ it.second }, { it.first })
}