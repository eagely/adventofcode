package solutions.y2017

import Solution
import java.io.File
import utils.*

class Day17 : Solution(2017) {

    override fun solvePart1(input: File): Any {
        val buf = arrayListOf(0)
        val step = input.text.toInt()
        var pos = 0
        for (i in 1..2017) {
            pos = (pos + step) % i + 1
            buf.add(pos, i)
        }
        return buf[(pos + 1) % buf.size]
    }

    override fun solvePart2(input: File): Any {
        var res = 0
        val step = input.text.toInt()
        var pos = 0
        for (i in 1..50.mega) {
            pos = (pos + step) % i + 1
            if (pos == 1) res = i
        }
        return res
    }
}