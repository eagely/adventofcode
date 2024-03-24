package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day16 : Solution(2016) {

    override fun solvePart1(input: File) = solve(input, 272)

    override fun solvePart2(input: File) = solve(input, 35651584)

    private fun solve(input: File, n: Int): String {
        val str = StringBuilder(input.text)
        while (str.length < n) {
            val rev = StringBuilder(str).reverse()
            rev.indices.forEach { rev[it] = if (rev[it] == '0') '1' else '0'}
            str.append('0').append(rev)
        }
        str.setLength(n)
        while (str.length % 2 == 0) {
            (str.indices step 2).forEach { str.setCharAt(it / 2, if (str[it] == str[it + 1]) '1' else '0') }
            str.setLength(str.length / 2)
        }
        return str.toString()
    }
}