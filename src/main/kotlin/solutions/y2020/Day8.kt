package solutions

import Solution
import utils.Utils.die
import java.io.File

class Day8 : Solution(2020) {

    override fun solvePart1(input: File) = runProgram(parse(input)).first

    override fun solvePart2(input: File): Any {
        val code = input.readLines().map { it.split(" ").let { (op, arg) -> op to arg.toInt() } }
        code.forEachIndexed { index, (op, arg) ->
            if (op != "acc") {
                val modifiedCode = code.toMutableList().apply {
                    this[index] = if (op == "jmp") "nop" to arg else "jmp" to arg
                }
                val (acc, terminated) = runProgram(modifiedCode)
                if (terminated) return acc
            }
        }
        die()
    }

    private fun runProgram(code: List<Pair<String, Int>>): Pair<Int, Boolean> {
        val seen = mutableSetOf<Int>()
        var idx = 0
        var acc = 0
        while (idx !in seen && idx < code.size) {
            seen += idx
            val (op, arg) = code[idx]
            when (op) {
                "acc" -> acc += arg
                "jmp" -> idx += arg - 1
            }
            idx++
        }
        return acc to (idx >= code.size)
    }

    private fun parse(input: File) = input.readLines().map { it.split(" ").let { (op, arg) -> op to arg.toInt() } }
}