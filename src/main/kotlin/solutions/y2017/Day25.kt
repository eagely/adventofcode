package solutions.y2017

import Solution
import utils.extractNumbers
import utils.sdanl
import utils.split
import utils.toChar
import java.io.File

class Day25 : Solution(2017) {

    override fun solvePart1(input: File): Any {
        val s = input.sdanl()
        val steps = s.first().last().extractNumbers().toInt()
        val states = s.drop(1).map { state -> state.map { it.dropLast(1).split().last() } }
            .associate { l -> l.first().toChar() to (listOf(0, 4)).map { a -> Triple(l[2 + a].toInt() == 1, l[3 + a].let { if (it == "left") -1 else 1 }, l[4 + a].toChar()) } }
        var c = s.first().first().removeSuffix(".").last()
        val tape = HashSet<Int>()
        var i = 0
        repeat(steps) {
            val (v, d, n) = states[c]!![if (i in tape) 1 else 0]
            if (v) tape.add(i) else tape.remove(i)
            i += d
            c = n
        }

        return tape.size
    }

    override fun solvePart2(input: File): Any {
        return "me when i solve the halting problem"
    }
}