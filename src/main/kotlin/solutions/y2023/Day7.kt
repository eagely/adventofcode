package solutions.y2023

import Solution
import utils.Utils.distinct
import utils.Utils.rl
import java.io.File

class Day7 : Solution(2023) {

    override fun solvePart1(input: File): Any {
        return input.rl().asSequence().map { it.split(" ") }.map { Triple(it[0], it[1], valueOf(it[0])) }.sortedWith { left, right ->
            if (left.third != right.third) left.third.compareTo(right.third)
            else compareHands(left.first, right.first, "23456789TJQKA")
        }.map { it.first to it.second.toInt() }.mapIndexed { i, it -> (i + 1) * it.second }.sum()
    }

    private fun valueOf(hand: String): Int {
        val uniques = hand.replace("23456789TQKA", "").distinct().count()
        val counts = hand.groupingBy { it }.eachCount()
        val score = when {
            counts.any { it.value == 5 } -> 6
            counts.any { it.value == 4 } -> 5
            uniques == 2 && counts.any { it.value == 3 } -> 4
            counts.any { it.value == 3 } -> 3
            uniques == 3 && counts.any { it.value == 2 } -> 2
            uniques == 4 -> 1
            else -> 0
        }
        return score
    }

    private fun compareHands(hand1: String, hand2: String, order: String) = hand1.zip(hand2).find { it.first != it.second }?.let { if (order.indexOf(it.first) < order.indexOf(it.second)) -1 else 1 } ?: 0

    override fun solvePart2(input: File): Any {
        return input.rl().asSequence().map { it.split(" ") }.map { Triple(it[0], it[1], valueOf(it[0].map { if (it == 'J') "23456789TQKA" else it.toString() }.joinToString(""))) }.sortedWith { left, right ->
            if (left.third != right.third) left.third.compareTo(right.third)
            else compareHands(left.first, right.first, "J23456789TQKA")
        }.map { it.first to it.second.toInt() }.mapIndexed { i, it -> (i + 1) * it.second }.sum()
    }
}
