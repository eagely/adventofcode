package solutions.y2024

import Solution
import utils.extractLongsSeparated
import utils.lines
import java.io.File
import kotlin.math.log10
import kotlin.math.pow

class Day7 : Solution(2024) {

    override fun solvePart1(input: File) = parse(input).sumOf { (t, n) ->
        val l = n.size - 1
        t.takeIf {
            (0..<(1 shl l)).any { i ->
                var res = n.first()
                for (j in 1..l) {
                    when ((i shr j - 1) and 1) {
                        0 -> res += n[j]
                        1 -> res *= n[j]
                    }
                    if (res > t) break
                }
                res == t
            }
        } ?: 0
    }

    override fun solvePart2(input: File) = parse(input).sumOf { (t, n) ->
        val l = n.size - 1
        t.takeIf {
            (0..<3.0.pow(l.toDouble()).toInt()).any { i ->
                var res = n.first()
                var c = i
                for (j in 1..l) {
                    when (c % 3) {
                        0 -> res += n[j]
                        1 -> res *= n[j]
                        2 -> res = concat(res, n[j])
                    }
                    c /= 3
                    if (res > t) break
                }
                res == t
            }
        } ?: 0
    }


    // almost 2x speedup
    private fun concat(a: Long, b: Long): Long {
        if (b < 10) return a * 10 + b
        if (b < 100) return a * 100 + b
        if (b < 1000) return a * 1000 + b
        if (b < 10000) return a * 10000 + b

        return 10.0.pow(log10(b.toDouble()).toInt() + 1).toLong() * a + b
    }

    private fun parse(input: File) =
        input.lines.map { line -> line.extractLongsSeparated().let { it.first() to it.drop(1) } }
}