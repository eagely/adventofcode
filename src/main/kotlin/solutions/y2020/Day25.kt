package solutions.y2020

import Solution
import utils.Utils.pm
import utils.Utils.rll
import java.io.File

class Day25 : Solution(2020) {

    override fun solvePart1(input: File): Any {
        val (door, card) = input.rll()
        return generateSequence(Pair(1L, 1L)) { (cd, dd) -> Pair(hash(cd, 7), hash(dd, door)) }.first { it.first == card }.second
    }

    override fun solvePart2(input: File): Any {
        return "time to do 2021 im so not excited for ALU"
    }

    private fun hash(input: Long, subj: Long) = (input * subj) pm 20201227L
}