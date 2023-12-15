package solutions.y2023

import Solution
import utils.Utils.after
import utils.Utils.before
import utils.Utils.rt
import java.io.File

class Day15 : Solution(2023) {

    override fun solvePart1(input: File) = input.rt().split(",").sumOf { hash(it) }

    override fun solvePart2(input: File): Any {
        val boxes = List(256) { mutableListOf<Pair<String, Int>>() }
        val lenses = input.rt().split(",")
        lenses.forEach { lens ->
            val label = lens.before("=").before("-")
            val hash = hash(label)
            if (lens[label.length] == '-') boxes[hash].removeAll { it.first == label }
            else (label to lens.after("=").toInt()).let { new -> boxes[hash].indexOfFirst { it.first == new.first }.let { if (it == -1) boxes[hash].add(new) else boxes[hash][it] = new } }
        }
        return boxes.withIndex().sumOf { (b, ls) -> ls.withIndex().sumOf { (s, l) -> (b + 1) * (s + 1) * l.second } }
    }

    private fun hash(s: String): Int {
        var cur = 0
        for (c in s) {
            cur += c.code
            cur *= 17
            cur %= 256
        }
        return cur
    }
}