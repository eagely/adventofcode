package solutions.y2020

import Solution
import utils.Utils.extractNumbers
import utils.Utils.sdanl
import java.io.File

class Day22 : Solution(2020) {

    override fun solvePart1(input: File): Any {
        val (p1, p2) = input.sdanl().map { ArrayDeque(it.drop(1).map { it.extractNumbers().toInt() }) }

        while (p1.isNotEmpty() && p2.isNotEmpty()) {
            val c1 = p1.removeFirst()
            val c2 = p2.removeFirst()
            if (c1 > c2) {
                p1.addLast(c1)
                p1.addLast(c2)
            } else {
                p2.addLast(c2)
                p2.addLast(c1)
            }
        }

        return p1.ifEmpty { p2 }.let { it.foldIndexed(0) { i, acc, x -> acc + (it.size - i) * x } }
    }

    override fun solvePart2(input: File): Any {
        val (ip1, ip2) = input.sdanl().map { ArrayDeque(it.drop(1).map { it.extractNumbers().toInt() }) }
        val seen = hashSetOf<Pair<List<Int>, List<Int>>>()

        fun play(p1: ArrayDeque<Int>, p2: ArrayDeque<Int>): Pair<Int, ArrayDeque<Int>> {
            while (p1.isNotEmpty() && p2.isNotEmpty()) {
                if (!seen.add(p1 to p2)) break
                val c1 = p1.removeFirst()
                val c2 = p2.removeFirst()

                if (p1.size >= c1 && p2.size >= c2) {
                    if (play(ArrayDeque(p1.take(c1)), ArrayDeque(p2.take(c2))).first == 1) {
                        p1.addLast(c1)
                        p1.addLast(c2)
                    } else {
                        p2.addLast(c2)
                        p2.addLast(c1)
                    }
                } else {
                    if (c1 > c2) {
                        p1.addLast(c1)
                        p1.addLast(c2)
                    } else {
                        p2.addLast(c2)
                        p2.addLast(c1)
                    }
                }
            }
            // i have no idea why this works and it might not work for every input
            return if (p1.isNotEmpty()) 1 to p1 else 2 to p2
        }

        return play(ip1, ip2).second.let { winner -> winner.foldIndexed(0) { i, acc, x -> acc + (winner.size - i) * x } }
    }
}