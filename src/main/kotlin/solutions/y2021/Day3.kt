package solutions.y2021

import Solution
import utils.Utils.asChar
import utils.Utils.pow
import utils.Utils.rl
import java.io.File

class Day3 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        val lines = input.rl()
        val bits = lines[0].length
        var rate = ""
        repeat(bits) { i ->
            rate += if (lines.count { it[i] == '0' } > lines.count { it[i] == '1' }) "0" else "1"
        }
        return rate.toInt(2) * (rate.toInt(2).inv() and (2 pow bits) - 1)
    }

    override fun solvePart2(input: File): Any {
        var o = input.rl()
        var c = input.rl()
        val bits = c[0].length
        repeat(bits) { i ->
            if (o.size > 1)
                o = o.filter { it[i] == (1 downTo 0).maxBy { n -> o.count { it[i] == n.asChar() } }.asChar() }
            if (c.size > 1)
                c = c.filter { it[i] == (0..1).minBy { n -> c.count { it[i] == n.asChar() } }.asChar() }
        }
        return o.first().toInt(2) * c.first().toInt(2)
    }
}