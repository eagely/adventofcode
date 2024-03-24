package solutions.y2016

import Solution
import utils.md5
import utils.text
import java.io.File
import kotlin.collections.ArrayDeque

class Day14 : Solution(2016) {

    override fun solvePart1(input: File) = solve(input, String::md5)

    override fun solvePart2(input: File) = solve(input, ::stretch)

    private fun solve(input: File, hashFunction: (String) -> String): Int {
        val salt = input.text
        var n = 0
        val queue = ArrayDeque((0..1000).map { hashFunction("$salt$it") })

        repeat(64) {
            while (true) {
                val hash = queue.removeFirst()
                queue.add(hashFunction("$salt${n++}"))
                val con = hash.containsLength(3)
                if (con != null && queue.any { it.containsLength(5, con) != null }) break
            }
        }
        return n - 1002
    }

    private fun stretch(input: String): String {
        var new = input
        repeat(2017) {
            new = new.md5()
        }
        return new
    }

    private fun String.containsLength(n: Int, of: Char? = null): Char? {
        for (i in 0 until this.length - n + 1) {
            val substring = this.substring(i, i + n)
            val cmp = of ?: this[i]
            if (substring.all { it == cmp }) return cmp
        }
        return null
    }
}