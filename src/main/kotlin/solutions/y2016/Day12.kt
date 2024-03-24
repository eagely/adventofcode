package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day12 : Solution(2016) {

    override fun solvePart1(input: File) = run(input, mutableMapOf<String, Int>().withDefault { 0 })

    override fun solvePart2(input: File) = run(input, mutableMapOf("c" to 1).withDefault { 0 })

    private fun MutableMap<String, Int>.getReg(key: String) = key.toIntOrElse { this.getValue(key) }

    private fun run(input: File, regs: MutableMap<String, Int>): Int {
        val prog = input.lines.map { it.split() }
        var i = 0
        while (i in prog.indices) {
            val v = prog[i].drop(1)
            when (prog[i][0]) {
                "cpy" -> regs[v.last()] = regs.getReg(v.first())
                "inc" -> regs[v.first()] = regs.getValue(v.first()) + 1
                "dec" -> regs[v.first()] = regs.getValue(v.first()) - 1
                "jnz" -> if (regs.getReg(v.first()) != 0) i += v.last().toInt() - 1
            }
            i++
        }
        return regs.getValue("a")
    }
}