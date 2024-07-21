package solutions.y2015

import Solution
import java.io.File
import utils.*

class Day23 : Solution(2015) {

    data class Instruction(val op: String, val args: List<String>)

    override fun solvePart1(input: File) = run(input.lines.map { it.remove('+').split(", ", " ").let { Instruction(it[0], it.drop(1)) } }, hashMapOf("a" to 0, "b" to 0))

    override fun solvePart2(input: File) = run(input.lines.map { it.remove('+').split(", ", " ").let { Instruction(it[0], it.drop(1)) } }, hashMapOf("a" to 1, "b" to 0))

    private fun run(prog: List<Instruction>, regs: HashMap<String, Int>): Int {
        var i = 0
        while (i in prog.indices) {
            val instr = prog[i]
            val arg1 = instr.args[0]
            val arg2 = if (instr.args.size == 2) instr.args[1] else ""
            when (instr.op) {
                "hlf" -> regs[arg1] = regs[arg1]!! / 2
                "tpl" -> regs[arg1] = regs[arg1]!! * 3
                "inc" -> regs[arg1] = regs[arg1]!! + 1
                "jmp" -> i += arg1.toInt() - 1
                "jie" -> i += if (regs[arg1]!! % 2 == 0) arg2.toInt() - 1 else 0
                "jio" -> i += if (regs[arg1]!! == 1) arg2.toInt() - 1 else 0
            }
            i++
        }
        return regs["b"]!!
    }
}