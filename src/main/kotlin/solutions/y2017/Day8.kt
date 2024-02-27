package solutions.y2017

import Solution
import java.io.File
import utils.*
import kotlin.math.max

class Day8 : Solution(2017) {

    data class Instruction(val r: String, val op: String, val v: Int, val cr: String, val cop: String, val cv: Int)

    private val opMap = mapOf(">" to { a: Int, b: Int -> a > b }, "<" to { a: Int, b: Int -> a < b }, ">=" to { a: Int, b: Int -> a >= b }, "<=" to { a: Int, b: Int -> a <= b }, "==" to { a: Int, b: Int -> a == b }, "!=" to { a: Int, b: Int -> a != b })

    override fun solvePart1(input: File): Any {
        val regs = HashMap<String, Int>()
        for (i in parse(input)) {
            val (r, op, v, cr, cop, cv) = i
            if (opMap[cop]!!(regs.getOrDefault(cr, 0), cv)) regs[r] = regs.getOrDefault(r, 0) + if (op == "inc") v else -v
        }
        return regs.maxOf { it.value }
    }

    override fun solvePart2(input: File): Any {
        val regs = HashMap<String, Int>()
        var max = 0
        for (i in parse(input)) {
            val (r, op, v, cr, cop, cv) = i
            if (opMap[cop]!!(regs.getOrDefault(cr, 0), cv)) regs[r] = regs.getOrDefault(r, 0) + if (op == "inc") v else -v
            max = max(max, regs.maxOfOrNull { it.value } ?: 0)
        }
        return max
    }

    fun parse(input: File) = input.lines.map { it.remove("if ").split().let { Instruction(it[0], it[1], it[2].toInt(), it[3], it[4], it[5].toInt()) } }
}