package solutions.y2015

import Solution
import utils.text
import java.io.File

class Day10 : Solution(2015) {

    override fun solvePart1(input: File) = play(input, 40)

    override fun solvePart2(input: File) = play(input, 50)

    private fun play(input: File, rounds: Int): Int {
        var cur = input.text
        repeat(rounds) {
            val builder = StringBuilder()
            var count = 1
            for (i in 1 until cur.length) {
                if (cur[i] == cur[i - 1]) count++
                else {
                    builder.append(count).append(cur[i - 1])
                    count = 1
                }
            }
            builder.append(count).append(cur.last())
            cur = builder.toString()
        }
        return cur.length
    }
}