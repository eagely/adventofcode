package solutions.y2017

import Solution
import utils.extractNumbers
import utils.toPair
import java.io.File

class Day15 : Solution(2017) {

    override fun solvePart1(input: File) = generateSequence(parse(input)) { (a, b) -> next(a, 16807) to next(b, 48271) }.take(40000000).count { (a, b) -> a and 0xffff == b and 0xffff }

    override fun solvePart2(input: File) = generateSequence(parse(input)) { (a, b) -> next(a, 16807, 4) to next(b, 48271, 8) }.take(5000000).count { (a, b) -> a and 0xffff == b and 0xffff }

    private fun next(i: Long, f: Long, m: Long = 1): Long {
        var v = i
        do { v = (v * f) % 2147483647 } while (v % m != 0L)
        return v
    }

    private fun parse(input: File) = input.readLines().map { it.extractNumbers().toLong() }.toPair()
}