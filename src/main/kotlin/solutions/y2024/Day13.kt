package solutions.y2024

import Solution
import utils.extendedGcd
import utils.extractNumbersSeparated
import utils.sdanl
import java.io.File

class Day13 : Solution(2024) {
    private data class Point(val x: Long, val y: Long)

    override fun solvePart1(input: File): Any = solve(input)

    override fun solvePart2(input: File): Any = solve(input, 10000000000000)

    private fun solve(input: File, addend: Long = 0): Long {
        val points = input.sdanl().map { line ->
            listOf(
                line[0].extractNumbersSeparated().let { Point(it[0].toLong(), it[1].toLong()) },
                line[1].extractNumbersSeparated().let { Point(it[0].toLong(), it[1].toLong()) },
                line[2].extractNumbersSeparated().let { Point(it[0].toLong() + addend, it[1].toLong() + addend) }
            )
        }
        return points.sumOf {
            val (a, b, c) = it
            val (n, m) = magic(a, b, c) ?: return@sumOf 0L
            n * 3 + m
        }
    }

    private fun magic(a: Point, b: Point, c: Point): Point? {
        val (gcd, x0, y0) = extendedGcd(a.x, b.x)
        if (c.x % gcd != 0L) return null

        // Find n and m such that n * a + m * b = c
        val x = c.x / gcd
        val nx = x0 * x
        val mx = y0 * x
        val k = (c.y - (nx * a.y + mx * b.y)) / ((b.x * a.y - a.x * b.y) / gcd)
        val n = nx + k * (b.x / gcd)
        val m = mx - k * (a.x / gcd)

        return Point(n, m).takeIf { n * a.x + m * b.x == c.x && n * a.y + m * b.y == c.y }
    }
}