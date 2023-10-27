package solutions.y2022
import Solution
import java.io.File

import java.util.*

class Day4 : Solution(2022) {


    override fun solvePart1(input: File): String {
        val lines = input.readLines()
        return getContainedTasks(lines).toString()
    }

    override fun solvePart2(input: File): String {
        val lines = input.readLines()
        return getOverlappingTasks(lines).toString()
    }

    private fun getContainedTasks(input: List<String>): Int {
        val ranges = getRanges(input)
        var amount = 0
        for (i in ranges.indices)
            if (ranges[i][0].all { it in ranges[i][1] } || ranges[i][1].all { it in ranges[i][0] })
                amount++
        return amount
    }

    private fun getOverlappingTasks(input: List<String>): Int {
        val ranges = getRanges(input)
        var amount = 0
        for (i in ranges.indices)
            if (ranges[i][0].any { it in ranges[i][1] })
                amount++
        return amount
    }

    private fun getRanges(input: List<String>): LinkedList<List<IntRange>> {
        val ranges = LinkedList<List<IntRange>>()
        for (i in input.indices) {
            ranges.add(
                listOf(
                    IntRange(
                        input[i].substringBefore("-").toInt(),
                        input[i].substring(input[i].indexOf("-") + 1, input[i].indexOf(",")).toInt()
                    ), IntRange(
                        input[i].substring(input[i].indexOf(",") + 1, input[i].lastIndexOf("-")).toInt(),
                        input[i].substringAfterLast("-").toInt()
                    )
                )
            )
        }
        return ranges
    }
}
