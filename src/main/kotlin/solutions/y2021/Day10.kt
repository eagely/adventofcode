package solutions.y2021

import Solution
import utils.Utils.remove
import utils.Utils.rl
import java.io.File

class Day10 : Solution(2021) {

    override fun solvePart1(input: File) = input.rl().sumOf {
        var cur = it
        while (cur.isNotEmpty()) {
            val old = cur
            cur = cur.remove("()", "[]", "{}", "<>")
            if (cur == old) return@sumOf if (cur.any { it in ")]}>" }) mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)[cur.remove('(', '[', '{', '<').first()]!! else 0
        }
        0
    }

    override fun solvePart2(input: File): Any {
        val lines = input.rl().filter {
            var cur = it
            while (cur.isNotEmpty()) {
                val old = cur
                cur = cur.remove("()", "[]", "{}", "<>")
                if (cur == old) return@filter cur.none { it in ")]}>" }
            }
            false
        }

        val mapping = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

        val counts = hashMapOf(')' to 0, ']' to 0, '}' to 0, '>' to 0)

        val scores = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

        val out = lines.map {
            var res = ""
            val iter = it.reversed().iterator()
            while (iter.hasNext()) {
                var cur = iter.next()
                if (cur in mapping.keys) counts[mapping[cur]!!] = counts[mapping[cur]!!]!! - 1 else counts[cur] = counts[cur]!! + 1
                if (cur !in counts.keys) cur = mapping[cur]!!
                if (counts[cur] == -1) {
                    res += cur
                    counts[cur] = 0
                }
            }
            res
        }
        val points = out.map { it.fold(0L) { acc, c -> acc * 5 + scores[c]!!  } }
        return points.first { v -> points.count { it > v } == points.count { it < v }}
    }
}