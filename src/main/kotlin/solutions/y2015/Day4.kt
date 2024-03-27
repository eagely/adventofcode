package solutions.y2015

import Solution
import java.io.File
import utils.*

class Day4 : Solution(2015) {

    private var six: Int? = null

    override fun solvePart1(input: File): Any {
        val salt = input.text
        var i = 1
        return generateSequence { "$salt${i++}".md5() }.withIndex().first {
            if (it.value.startsWith("000000") && six == null) six = it.index
            it.value.startsWith("00000")
        }.index + 1
    }

    override fun solvePart2(input: File): Any {
        six?.let { return it }
        val salt = input.text
        var i = six ?: 1
        return generateSequence { "$salt${i++}".md5() }.withIndex().first { it.value.startsWith("000000") }.index + 1
    }
}