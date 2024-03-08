package solutions.y2017

import Solution
import utils.annotations.NoTest
import java.io.File
import kotlin.collections.set
import utils.*

class Day18 : Solution(2017) {

    data class Instruction(val cmd: String, val reg: String, val value: String)

    @NoTest
    override fun solvePart1(input: File): Any {
        val instr = parse(input)
        val regs = mutableMapOf<String, Long>().withDefault { 0 }
        var freq = 0L
        var i = 0L

        while (true) {
            val cur = instr[i.toInt()]
            when (cur.cmd) {
                "snd" -> freq = regs.getReg(cur.reg)
                "set" -> regs[cur.reg] = regs.getReg(cur.value)
                "add" -> regs[cur.reg] = regs.getReg(cur.reg) + regs.getReg(cur.value)
                "mul" -> regs[cur.reg] = regs.getReg(cur.reg) * regs.getReg(cur.value)
                "mod" -> regs[cur.reg] = regs.getReg(cur.reg) % regs.getReg(cur.value)
                "rcv" -> if (regs.getReg(cur.reg) != 0L) return freq
                "jgz" -> if (regs.getReg(cur.reg) > 0L) i += regs.getReg(cur.value) - 1
            }
            i++
        }
    }

    override fun solvePart2(input: File): Any {
        val instr = parse(input)
        val queues = Array(2) { ArrayDeque<Long>() }
        val regs = Array(2) { mutableMapOf<String, Long>().withDefault { 0 } }
        regs[1]["p"] = 1
        val i = Array(2) { 0L }
        var c = 0

        fun perform(p: Int): Boolean {
            while (true) {
                if (i[p] !in instr.indices) return false
                val cur = instr[i[p].toInt()]
                when (cur.cmd) {
                    "snd" -> {
                        if (p == 1) c++
                        queues[1 - p].add(regs[p].getReg(cur.reg))
                    }
                    "set" -> regs[p][cur.reg] = regs[p].getReg(cur.value)
                    "add" -> regs[p][cur.reg] = regs[p].getReg(cur.reg) + regs[p].getReg(cur.value)
                    "mul" -> regs[p][cur.reg] = regs[p].getReg(cur.reg) * regs[p].getReg(cur.value)
                    "mod" -> regs[p][cur.reg] = regs[p].getReg(cur.reg) % regs[p].getReg(cur.value)
                    "rcv" -> regs[p][cur.reg] = queues[p].removeFirstOrNull() ?: return true
                    "jgz" -> if (regs[p].getReg(cur.reg) > 0L) i[p] += regs[p].getReg(cur.value) - 1
                }
                i[p]++

            }
        }
        while (perform(0) && perform(1) && queues.any { it.isNotEmpty() });
        return c
    }

    private fun Map<String, Long>.getReg(key: String) = key.toLongOrNull() ?: this.getValue(key)

    private fun parse(input: File) = input.lines.map { it.split().let { Instruction(it[0], it[1], if (it.size == 2) "" else it[2]) } }
}