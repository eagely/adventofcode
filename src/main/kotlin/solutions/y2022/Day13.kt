package solutions.y2022

import Solution
import java.io.File

class Day13 : Solution(2022) {

    private fun String.parse(): Any {
        val stack: MutableList<MutableList<Any>> = mutableListOf(mutableListOf())
        replace("]", ",}").replace("[", "{,").replace(",,", ",").split(",").forEach { s ->
            when (s) {
                "{" -> stack.last().add(mutableListOf<Any>().also { stack.add(it) })
                "}" -> stack.removeLast()
                else -> stack.last().add(s.toInt())
            }
        }
        return stack.first().first()
    }

    private fun compare(a: Any, b: Any): Int = when {
        a is Int && b is Int -> a.compareTo(b)
        else -> {
            val aList = a as? MutableList<*> ?: mutableListOf(a)
            val bList = b as? MutableList<*> ?: mutableListOf(b)
            aList.zip(bList).firstNotNullOfOrNull { (u, v) -> compare(u!!, v!!).takeIf { it != 0 } } ?: aList.size.compareTo(bList.size)
        }
    }

    override fun solvePart1(input: File): String =
        input.readLines()
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.parse() }
            .chunked(2)
            .withIndex()
            .filter { (_, pair) -> compare(pair[0], pair[1]) <= 0 }
            .sumOf { (i, _) -> i + 1 }
            .toString()

    override fun solvePart2(input: File): String {
        val lists = input.readLines()
            .filter { it.isNotBlank() }
            .map { it.parse() }
            .toMutableList()

        val distressSignals = listOf(listOf(listOf(2)), listOf(listOf(6)))
        lists.addAll(distressSignals)
        lists.sortWith(::compare)

        return ((lists.indexOf(distressSignals[0]) + 1) * (lists.indexOf(distressSignals[1]) + 1)).toString()
    }
}
