package solutions.y2018

import Solution
import java.io.File
import utils.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Day4 : Solution(2018) {

    data class Entry(val time: String, val action: String)

    override fun solvePart1(input: File) = parseEepyTimes(input.lines).maxBy { (_, t) -> t.sumOf { it.size } }.let { (g, t) -> g * (0..60).maxBy { minute -> t.filter { minute in it }.size } }

    override fun solvePart2(input: File) = parseEepyTimes(input.lines).maxBy { (_, t) -> (0..60).maxOf { minute -> t.filter { minute in it }.size } }.let { (g, t) -> g * (0..60).maxBy { minute -> t.filter { minute in it }.size } }

    private fun parseEepyTimes(input: List<String>): Map<Int, List<IntRange>> {
        val sorted = input.map { it.drop(1).split("] ").let { (a, b) -> Entry(a, b) } }.sortedBy { LocalDateTime.parse(it.time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) }
        val eepyTimes = hashMapOf<Int, List<IntRange>>()
        for (s in sorted.separate { '#' in it.action }) {
            val guard = s.first().action.extractNumbers().toInt()
            val list = eepyTimes.getOrDefault(guard, emptyList()).toMutableList()
            var start = 0
            for (a in s.drop(1)) {
                if ("falls asleep" in a.action) start = a.time.after(":").toInt()
                else list += start..<a.time.after(":").toInt()
            }
            eepyTimes[guard] = list
        }
        return eepyTimes
    }
}