package solutions.y2018

import Solution
import utils.annotations.NoTest
import utils.extractNumbersSeparated
import utils.split
import utils.text
import java.io.File

class Day16 : Solution(2018) {

    private fun List<Int>.op(c: Int, operation: Int): List<Int> = mapIndexed { index, value -> if (index == c) operation else value }
    private fun addr(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a] + l[b])
    private fun addi(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a] + b)
    private fun mulr(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a] * l[b])
    private fun muli(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a] * b)
    private fun banr(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a] and l[b])
    private fun bani(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a] and b)
    private fun borr(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a] or l[b])
    private fun bori(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a] or b)
    private fun setr(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, l[a])
    private fun seti(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, a)
    private fun gtir(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, if (a > l[b]) 1 else 0)
    private fun gtri(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, if (l[a] > b) 1 else 0)
    private fun gtrr(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, if (l[a] > l[b]) 1 else 0)
    private fun eqir(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, if (a == l[b]) 1 else 0)
    private fun eqri(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, if (l[a] == b) 1 else 0)
    private fun eqrr(l: List<Int>, a: Int, b: Int, c: Int): List<Int> = l.op(c, if (l[a] == l[b]) 1 else 0)
    private val operations: Array<List<Int>.(Int, Int, Int) -> List<Int>> = arrayOf(
        ::addr, ::addi, ::mulr, ::muli, ::banr, ::bani, ::borr, ::bori, ::setr, ::seti, ::gtir, ::gtri, ::gtrr, ::eqir, ::eqri, ::eqrr
    )

    data class Sample(val before: List<Int>, val op: List<Int>, val after: List<Int>)

    @NoTest
    override fun solvePart1(input: File) = parse(input).first.count { detectOperation(it) >= 3 }

    @NoTest
    override fun solvePart2(input: File): Any {
        val (samples, program) = parse(input)
        var register = listOf(0, 0, 0, 0)
        val opcodes = detectOpcodes(samples)
        for (p in program) {
            register = opcodes[p[0]](register, p[1], p[2], p[3])
        }
        return register[0]
    }

    private fun detectOperation(s: Sample) = operations.count { it(s.before, s.op[1], s.op[2], s.op[3]) == s.after }

    private fun detectOpcodes(samples: Set<Sample>): List<List<Int>.(Int, Int, Int) -> List<Int>> {
        val opcodes = MutableList(16) { -1 }
        val possibleOpcodes = (0..15).map { o -> (0..15).filter { i -> samples.filter { it.op[0] == o }.all { operations[i](it.before, it.op[1], it.op[2], it.op[3]) == it.after } }.toMutableSet() }

        while (-1 in opcodes) {
            val i = possibleOpcodes.indexOfFirst { it.size == 1 }
            val opcode = possibleOpcodes[i].first()
            opcodes[i] = opcode
            possibleOpcodes.forEach { it -= opcode }
        }

        return opcodes.map { operations[it] }
    }

    private fun parse(input: File): Pair<Set<Sample>, List<List<Int>>> {
        val text = input.text
        return text.split("\n\n").map { it.split("\n") }.dropLast(2).map { block -> block.map { line -> line.extractNumbersSeparated() }.let { Sample(it[0], it[1], it[2]) } }
            .toSet() to text.split("\n\n").last().split('\n').map { line -> line.split().map { it.toInt() } }
    }
}