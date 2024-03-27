package solutions.y2015

import Solution
import utils.*
import java.io.File

class Day7 : Solution(2015) {

    val ops = mapOf(
        "AND" to { a: Int, b: Int -> a and b },
        "OR" to { a: Int, b: Int -> a or b },
        "LSHIFT" to { a: Int, b: Int -> a shl b },
        "RSHIFT" to { a: Int, b: Int -> a shr b },
    )

    override fun solvePart1(input: File) = run(parse(input))

    override fun solvePart2(input: File) = run(parse(input).map { if (it[1] == "b") listOf(run(parse(input)).toString(), "b") else it })

    private fun parse(input: File) = input.lines.map { it.split(" -> ") }

    private fun run(lines: List<List<String>>): Int {
        val regs = mutableMapOf<String, Int>().apply { this["b"] = 16076 }
        var i = 0
        while (regs["a"] == null) {
            val (instr, r) = lines at i++
            regs[r] = when {
                instr.isNumber() -> instr.toInt()
                "NOT" in instr -> regs.getReg(instr.after(" "))?.inv() ?: continue
                instr.isLowercase() -> regs.getReg(instr) ?: continue
                else -> {
                    val (f, op, s) = instr.split()
                    ops[op]!!(regs.getReg(f) ?: continue, regs.getReg(s) ?: continue)
                }
            }
        }
        return regs["a"]!!
    }

    private fun Map<String, Int>.getReg(s: String) = if (s.isNumber()) s.toInt() else this[s]
}