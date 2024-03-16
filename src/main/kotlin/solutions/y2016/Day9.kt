package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day9 : Solution(2016) {

    override fun solvePart1(input: File): Any {
        val text = input.text.remove("(\\s|\\t|\\n)".toRegex())
        val it = text.withIndex().iterator()
        var count = 0
        while (it.hasNext()) {
            val (i, c) = it.next()
            if (c == '(') {
                val (length, times) = text.substring(i + 1, text.indexOf(')', i)).extractNumbersSeparated()
                while (it.next().value != ')') {}
                count += length * times
                it.skip(length)
            }
            else {
                count++
            }
        }
        return count
    }

    override fun solvePart2(input: File): Any {
        val text = input.text.remove("(\\s|\\t|\\n)".toRegex())
        fun decompress(text: String): Long {
            val it = text.withIndex().iterator()
            var count = 0L
            while (it.hasNext()) {
                var (i, c) = it.next()
                if (c == '(') {
                    val (length, times) = text.substring(i + 1, text.indexOf(')', i)).extractNumbersSeparated()
                    while (true) {
                        i++
                        if (it.next().value == ')') break
                    }
                    count += decompress(text.substring(i + 1, i + 1 + length)) * times
                    it.skip(length)
                }
                else {
                    count++
                }
            }
            return count
        }
        return decompress(text)
    }
}