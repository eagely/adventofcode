package solutions.y2024

import Solution
import utils.annotations.NoTest
import utils.join
import utils.sdanl
import java.io.File

class Day24 : Solution(2024) {

    private enum class Operation { AND, OR, XOR }

    private data class Gate(val a: String, val b: String, val op: Operation, var c: String)

    override fun solvePart1(input: File) = parse(input).let { (gates, registers) -> run(gates, registers) }

    @NoTest
    override fun solvePart2(input: File): Any {
        val (gates, registers) = parse(input)

        val nxz = gates.filter { it.c.first() == 'z' && it.c != "z45" && it.op != Operation.XOR }
        val xnz = gates.filter { it.a.first() !in "xy" && it.b.first() !in "xy" && it.c.first() != 'z' && it.op == Operation.XOR }

        for (i in xnz) {
            val b = nxz.first { it.c == gates.firstZThatUsesC(i.c) }
            val temp = i.c
            i.c = b.c
            b.c = temp
        }

        val falseCarry = (getWiresAsLong(registers, 'x') + getWiresAsLong(registers, 'y') xor run(gates, registers)).countTrailingZeroBits().toString()
        return (nxz + xnz + gates.filter { it.a.endsWith(falseCarry) && it.b.endsWith(falseCarry) }).map { it.c }.sorted().join(",")
    }

    private fun List<Gate>.firstZThatUsesC(c: String): String? {
        val x = filter { it.a == c || it.b == c }
        x.find { it.c.startsWith('z') }?.let { return "z" + (it.c.drop(1).toInt() - 1).toString().padStart(2, '0') }
        return x.firstNotNullOfOrNull { firstZThatUsesC(it.c) }
    }

    private fun run(gates: List<Gate>, registers: MutableMap<String, Int>): Long {
        val exclude = HashSet<Gate>()

        while (exclude.size != gates.size) {
            val available = gates.filter { a ->
                a !in exclude && gates.none { b ->
                    (a.a == b.c || a.b == b.c) && b !in exclude
                }
            }
            for ((a, b, op, c) in available) {
                val v1 = registers.getOrDefault(a, 0)
                val v2 = registers.getOrDefault(b, 0)
                registers[c] = when (op) {
                    Operation.AND -> v1 and v2
                    Operation.OR -> v1 or v2
                    Operation.XOR -> v1 xor v2
                }
            }
            exclude.addAll(available)
        }

        return getWiresAsLong(registers, 'z')
    }

    private fun getWiresAsLong(registers: MutableMap<String, Int>, type: Char) = registers.filter { it.key.startsWith(type) }.toList().sortedBy { it.first }.map { it.second }.join("").reversed().toLong(2)

    private fun parse(input: File): Pair<MutableList<Gate>, MutableMap<String, Int>> {
        val (initial, path) = input.sdanl()
        val gates = path.map { it.split(" ").let { Gate(it[0], it[2], Operation.valueOf(it[1]), it[4]) } }.toMutableList()
        val registers = initial.associate { it.split(": ").let { it.first() to it.last().toInt() } }
        return gates to registers.toMutableMap()
    }
}