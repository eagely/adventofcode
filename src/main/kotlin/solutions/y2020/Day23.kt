package solutions.y2020

import Solution
import utils.Utils.asInt
import utils.Utils.product
import utils.Utils.rt
import java.io.File

class Day23 : Solution(2020) {

    override fun solvePart1(input: File) = List(input.rt()).play(100).let { cups -> generateSequence(cups.next) { it.next }.takeWhile { it != cups }.joinToString("") { it.value.toString() } }

    override fun solvePart2(input: File) = List(input.rt(), 1000000).play(10000000).take(2).map { it.value.toLong() }.product()

    data class Cup(val value: Int, var next: Cup? = null) {
        fun take(n: Int) = (1..n).runningFold(this) { cur, _ -> cur.next!! }.drop(1)
    }

    private class List(initial: String, length: Int = initial.length) {
        val data = List(length + 1) { Cup(it) }
        var head = data[initial.first().asInt()]
        val order = initial.map { it.asInt() } + (initial.length + 1..length)

        init {
            order.map { data[it] }.fold(data[initial.last().asInt()]) { previous, cup -> cup.also { previous.next = cup } }
            data[order.last()].next = data[order.first()]
        }

        fun play(rounds: Int): Cup {
            repeat(rounds) {
                val pickup = head.take(3)
                var p = head.value - 1
                while (p in pickup.map { it.value } || p == 0) p = if (p == 0) data.size - 1 else p - 1
                val dest = data[p]
                head.next = pickup.last().next
                pickup.last().next = dest.next
                dest.next = pickup.first()
                head = head.next!!
            }
            return data[1]
        }
    }
}