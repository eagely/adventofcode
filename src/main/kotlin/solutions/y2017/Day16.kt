package solutions.y2017

import Solution
import utils.*
import utils.annotations.NoTest
import java.io.File

class Day16 : Solution(2017) {

    @NoTest
    override fun solvePart1(input: File) = dance(input)

    @NoTest
    override fun solvePart2(input: File) = dance(input, 1.giga)

    private fun dance(input: File, times: Int = 1): String {
        val moves = input.text.split(',').map { it.first() to it.drop(1) }
        val programs = ('a'..'p').toMutableList()
        var pos = 0
        val visited = HashSet<String>()
        val order = ArrayList<String>()
        repeat(times) {
            for (m in moves) {
                when (m.first) {
                    's' -> pos -= m.second.toInt()
                    'x' -> programs.swap(m.second.extractNumbersSeparated().map { (pos + it) pm 16 }.toPair())
                    'p' -> programs.swap(m.second.split('/').map { it.toChar() }.toPair())
                }
            }
            if (!visited.add(programs.join())) return order[times % order.size - 1]
            order += programs.rotate(pos pm 16).join()
        }
        return programs.rotate(pos pm 16).join()
    }

    private fun MutableList<Char>.rotate(n: Int) = drop(n) + take(n)
}