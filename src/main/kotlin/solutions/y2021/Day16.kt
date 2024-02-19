package solutions.y2021

import Solution
import utils.hexToBin
import utils.productOf
import utils.rt
import java.io.File

class Day16 : Solution(2021) {

    data class Packet(val version: Int, val type: Int, val value: Long)

    override fun solvePart1(input: File): Any {
        fun process(binary: String, index: Int): Pair<Int, Int> {
            var i = index
            val version = binary.substring(i, i + 3).toInt(2)
            i += 6

            var sum = version

            if (binary.substring(i - 3, i).toInt(2) == 4) {
                do {
                    i += 5
                } while(binary[i - 5] != '0')
            } else {
                val ltid = binary[i].toString().toInt()
                i += 1

                if (ltid == 0) {
                    val len = binary.substring(i, i + 15).toInt(2)
                    i += 15
                    val end = i + len
                    while (i < end) {
                        val (ni, ns) = process(binary, i)
                        i = ni
                        sum += ns
                    }
                } else {
                    i += 11
                    repeat(binary.substring(i - 11, i).toInt(2)) {
                        val (ni, ns) = process(binary, i)
                        i = ni
                        sum += ns
                    }
                }
            }

            return i to sum
        }

        return process(input.rt().hexToBin(), 0).second
    }

    override fun solvePart2(input: File): Any {
        fun process(binary: String, index: Int): Pair<Int, Packet> {
            var i = index
            val version = binary.substring(i, i + 3).toInt(2)
            val tid = binary.substring(i + 3, i + 6).toInt(2)
            i += 6

            if (tid == 4) {
                var value = 0L
                do {
                    value = (value shl 4) + binary.substring(i + 1, i + 5).toLong(2)
                    i += 5
                } while (binary[i - 5] != '0')
                return i to Packet(version, tid, value)
            } else {
                val ltid = binary[i].toString().toInt()
                i += 1
                val sp = mutableListOf<Packet>()

                if (ltid == 0) {
                    val len = binary.substring(i, i + 15).toInt(2)
                    i += 15
                    val end = i + len
                    while (i < end) {
                        val (ni, p) = process(binary, i)
                        sp.add(p)
                        i = ni
                    }
                } else {
                    i += 11
                    repeat(binary.substring(i - 11, i).toInt(2)) {
                        val (ni, p) = process(binary, i)
                        sp.add(p)
                        i = ni
                    }
                }

                val value = when (tid) {
                    0 -> sp.sumOf { it.value }
                    1 -> sp.productOf { it.value }
                    2 -> sp.minOf { it.value }
                    3 -> sp.maxOf { it.value }
                    5 -> if (sp[0].value > sp[1].value) 1 else 0
                    6 -> if (sp[0].value < sp[1].value) 1 else 0
                    7 -> if (sp[0].value == sp[1].value) 1 else 0
                    else -> throw IllegalArgumentException("Unknown type ID")
                }
                return i to Packet(version, tid, value)
            }
        }

        return process(input.rt().hexToBin(), 0).second.value
    }
}