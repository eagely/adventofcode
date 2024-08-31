package solutions.y2018

import Solution
import utils.*
import utils.point.Point
import java.io.File

class Day11 : Solution(2018) {

    override fun solvePart1(input: File) = getBiggestSquare(input.text.toInt(), 3..3).let { "${it.first.x},${it.first.y}" }

    override fun solvePart2(input: File) = getBiggestSquare(input.text.toInt(), 1..300).let { "${it.first.x},${it.first.y},${it.second}" }

    private fun getBiggestSquare(serial: Int, sizeRange: IntRange): Pair<Point, Int> {
        val dataTable = (0..299).map { x -> (0..299).map { y -> ((x + 10) * y + serial) * (x + 10) / 100 % 10 - 5 } }
        val sums = Array(300) { IntArray(300) }
        for (x in 0..299) {
            for (y in 0..299) {
                sums[x][y] = sums.getOrZero(x - 1, y) + sums.getOrZero(x, y - 1) - sums.getOrZero(x - 1, y - 1) + dataTable[x][y]
            }
        }
        var max = 0
        lateinit var winner: Pair<Point, Int>
        for (s in sizeRange) {
            for (x in -1..<300 - s) {
                for (y in -1..<300 - s) {
                    val sum = sums[x + s][y + s] + sums.getOrZero(x, y) - sums.getOrZero(x + s, y) - sums.getOrZero(x, y + s)
                    if (sum > max) {
                        winner = Point(x + 1, y + 1) to s
                        max = sum
                    }
                }
            }
        }
        return winner
    }

    private fun Array<IntArray>.getOrZero(x: Int, y: Int): Int {
        if (x !in this.indices || y !in this[x].indices) return 0
        return this[x][y]
    }
}