package solutions.y2015

import Solution
import java.io.File
import utils.*

class Day25 : Solution(2015) {

    override fun solvePart1(input: File): Any {
        val (r, c) = input.text.extractLongsSeparated().toPair()
        val mod = 33554393L
        val n = r + c - 1
        return (252533L.modPow((n * (n-1) / 2 + c - 1), mod) * 20151125L) % mod
    }

    override fun solvePart2(input: File) = "time to suffer on intcode!! yay :("
}