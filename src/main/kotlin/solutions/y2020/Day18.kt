package solutions.y2020

import Solution
import utils.Utils.die
import utils.Utils.rl
import java.io.File

class Day18 : Solution(2020) {

    override fun solvePart1(input: File) = input.rl().sumOf { solve(it) }

    override fun solvePart2(input: File) = input.rl().sumOf { solve(it, '+') }

    private fun solve(expr: String, precedence: Char? = null): Long {
        val i = expr.iterator()
        fun eval(): Long {
            var cur: Long? = null
            var op: Char? = null
            while (i.hasNext()) {
                val next = i.nextChar()
                when {
                    next.isDigit() -> {
                        val n = next.toString().toLong()
                        cur = if (cur == null) n else op(cur, n, op)
                    }
                    next == '+' || next == '*' -> {
                        if (precedence == '+' && next == '*') return cur!! * eval()
                        op = next
                    }
                    next == '(' -> {
                        val n = eval()
                        cur = if (cur == null) n else op(cur, n, op)
                    }
                    next == ')' -> break
                }
            }
            return cur ?: 0
        }
        return eval()
    }

    private fun op(a: Long, b: Long, op: Char?) = when (op) {
        '+' -> a + b
        '*' -> a * b
        else -> die()
    }
}