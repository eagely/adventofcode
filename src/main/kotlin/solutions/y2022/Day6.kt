package solutions.y2022
import Solution
import java.io.File

class Day6 : Solution(2022) {


    override fun solvePart1(input: File): String {
        val lines = input.readText()
        return findFirstNonMatchingSet(lines, 4).toString()
    }

    override fun solvePart2(input: File): String {
        val lines = input.readText()
        return findFirstNonMatchingSet(lines, 14).toString()
    }

    private fun findFirstNonMatchingSet(input: String, size: Int): Int {
        var chars = input.substring(0, size)
        var matchingChars = 0
        var firstMatchPosition = 0
        for (i in size - 1 ..< input.length) {
            for (j in 0 ..< size)
                for (k in 0 ..< size)
                    if (chars[j] == chars[k] && j != k)
                        matchingChars++
            if (matchingChars == 0) {
                firstMatchPosition = input.indexOf(chars)
                break
            } else
                matchingChars = 0
            chars = chars.substring(1, size)
            chars += input[i]
        }
        return firstMatchPosition + size
    }
}
