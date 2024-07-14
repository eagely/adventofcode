package solutions.y2015

import java.io.File
import utils.*
import Solution

class Day11 : Solution(2015) {

    override fun solvePart1(input: File) = getPassword(input.text)

    override fun solvePart2(input: File) = getPassword(getPassword(input.text))

    private fun next(password: String): String {
        val result = StringBuilder(password)
        var i = result.length - 1
        while (i >= 0) {
            if (result[i] == 'z') {
                result[i] = 'a'
                i--
            } else {
                result[i]++
                if (result[i] in "iol") result[i]++
                break
            }
        }
        return result.toString()
    }

    private fun getPassword(init: String): String {
        var password = next(init)
        while ((password.any { it in "iol" } || password.windowed(3).none { it[0] + 1 == it[1] && it[1] + 1 == it[2] } || password.windowed(2).filter { it[0] == it[1] }.map { it[0] }.toList().distinct().size < 2)) {
            password = next(password)
        }
        return password
    }
}