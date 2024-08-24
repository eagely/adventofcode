package solutions.y2018

import Solution
import utils.*
import java.io.File

class Day9 : Solution(2018) {

    override fun solvePart1(input: File): Any {
        val (players, marbles) = input.text.extractNumbersSeparated()
        return run(players, marbles)
    }

    override fun solvePart2(input: File): Any {
        val (players, marbles) = input.text.extractNumbersSeparated()
        return run(players, marbles * 100)
    }

    private fun run(players: Int, marbles: Int): Long {
        val circle = arrayDequeOf(0L)
        val scores = LongArray(players) { 0 }
        var next = 1L
        for (m in 1..marbles) {
            if (next % 23 == 0L) {
                circle.rotate(-7)
                scores[m % players] += circle.removeLast() + next
            } else {
                circle.rotate(2)
                circle.addLast(next)
            }
            next++
            if (next > marbles) break
        }

        return scores.maxOf { it }
    }
}