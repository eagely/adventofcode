package solutions.y2020

import Solution
import utils.Utils.after
import utils.Utils.extractNumbersSeparated
import utils.Utils.rl
import utils.annotations.NoTest
import java.io.File

class Day14 : Solution(2020) {

    @NoTest
    override fun solvePart1(input: File): Any {
        val lines = input.rl()
        var and = 0L
        var or = 0L
        val mem = hashMapOf<Long, Long>()
        for (line in lines) {
            if (line.startsWith("mask")) {
                and = line.after("= ").replace('X', '1').toLong(2)
                or = line.after("= ").replace('X', '0').toLong(2)
            } else {
                val (addr, value) = line.extractNumbersSeparated().map { it.toLong() }
                mem[addr] = (value and and) or or
            }
        }
        return mem.values.sum()
    }

    override fun solvePart2(input: File): Any {
        val lines = input.rl()
        var mask = ""
        val mem = hashMapOf<Long, Long>()
        for (line in lines) {
            if ("mask" in line) mask = line.after("= ")
            else {
                val (addr, value) = line.extractNumbersSeparated().map { it.toLong() }
                mem.putAll(decode(mask, addr).associateWith { value })
            }
        }
        return mem.values.sum()
    }

    private fun decode(mask: String, originalAddress: Long): List<Long> {
        var res = listOf(originalAddress)
        for ((i, v) in mask.withIndex()) {
            val di = mask.length - i - 1
            when (v) {
                '1' -> res = res.map { it or (1L shl di) }
                'X' -> res = res.map { it or (1L shl di) } + res.map { it and (1L shl di).inv() }
            }
        }
        return res
    }
}
