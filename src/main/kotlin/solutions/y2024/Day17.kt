package solutions.y2024

import Solution
import utils.extractNumbers
import utils.extractNumbersSeparated
import utils.join
import utils.lines
import java.io.File

class Day17 : Solution(2024) {

    override fun solvePart1(input: File): Any {
        val (a, program) = input.lines.let { it[0].extractNumbers().toInt() to it.last().extractNumbersSeparated() }
        val regs = mutableListOf(a, 0, 0)
        var p = 0
        val out = mutableListOf<Int>()

        while (p in program.indices && p + 1 in program.indices) {
            val op = program[p]
            val operand = program[p + 1]
            when (op) {
                0 -> regs[0] /= 1 shl regs.combo(operand)
                1 -> regs[1] = regs[1] xor operand
                2 -> regs[1] = regs.combo(operand) % 8
                3 -> if (regs[0] != 0) p = operand - 2
                4 -> regs[1] = regs[1] xor regs[2]
                5 -> out += regs.combo(operand) % 8
                6 -> regs[1] = regs[0] / (1 shl regs.combo(operand))
                7 -> regs[2] = regs[0] / (1 shl regs.combo(operand))
            }
            p += 2
        }

        return out.join(",")
    }

    override fun solvePart2(input: File): Any {
        val g = input.lines.last().extractNumbersSeparated()

        fun guh(p: Int, r: Long, d: Int): Long {
            if (p < 0) return r
            if (d > 7) return -1

            val regs = mutableListOf((r shl 3).toInt() or d, 0, 0)
            var i = 0
            var w = 0

            while (i < g.size) {
                val c = g[i + 1]
                val o = regs.combo(c)

                when (g[i]) {
                    0 -> regs[0] = regs[0] shr o
                    1 -> regs[1] = regs[1] xor g[i + 1]
                    2 -> regs[1] = (o and 7)
                    3 -> i = if (regs[0] != 0) g[i + 1] - 2 else i
                    4 -> regs[1] = regs[1] xor regs[2]
                    5 -> {
                        w = o and 7
                        break
                    }
                    6 -> regs[1] = regs[0] shr o
                    7 -> regs[2] = regs[0] shr o
                }
                i += 2
            }

            if (w == g[p]) {
                val result = guh(p - 1, (r shl 3) or d.toLong(), 0)
                if (result != -1L) return result
            }

            return guh(p, r, d + 1)
        }

        return guh(g.size - 1, 0, 0)
    }

    private fun List<Int>.combo(operand: Int) = if (operand in 0..3) operand else get(operand - 4)
}