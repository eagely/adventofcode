package solutions.y2020

import Solution
import utils.Utils.rt
import java.io.File

class Day15 : Solution(2020) {

    override fun solvePart1(input: File): Any {
        val start = input.rt().split(",").mapIndexed { i, n -> n.toInt() to i + 1 }
        val map = hashMapOf<Int, Int>()
        map.putAll(start)
        var last = 0
        for (cur in start.size + 1..2020) {
            val next = if (last in map) cur - map[last]!! else 0
            map[last] = cur
            last = next
        }
        return map.filter { it.value == 2020 }.keys.first()
    }

    override fun solvePart2(input: File): Any {
        val start = input.rt().split(",").mapIndexed { i, n -> n.toInt() to i + 1 }
        val map = hashMapOf<Int, Int>()
        map.putAll(start)
        var last = 0
        for (cur in start.size + 1..30000000) {
            val next = if (last in map) cur - map[last]!! else 0
            map[last] = cur
            last = next
        }
        return map.filter { it.value == 30000000 }.keys.first()
    }
}
