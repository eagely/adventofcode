package solutions.y2016

import Solution
import java.io.File
import utils.*

class Day21 : Solution(2016) {

    override fun solvePart1(input: File) = "abcdefgh".scramble(input.lines)

    override fun solvePart2(input: File): Any {
        val lines = input.lines
        return "fbgdceah".toList().permutations().map { it.join() }.first { it.scramble(lines) == "fbgdceah" }
    }

    private fun String.scramble(lines: List<String>): String {
        var cur = this
        for (line in lines) {
            when {
                line.startsWith("swap position") -> {
                    val (x, y) = line.extractNumbersSeparated()
                    cur = cur.replaceAt(x, '0').replaceAt(y, cur[x]).replace('0', cur[y])
                }
                line.startsWith("swap letter") -> {
                    val (x, y) = line.split().let { it[2] to it[5] }
                    cur = cur.replace(x, "0").replace(y, x).replace("0", y)
                }
                line.startsWith("rotate left") -> {
                    val x = line.extractNumbers().toInt()
                    cur = cur.substring(x) + cur.substring(0, x)
                }
                line.startsWith("rotate right") -> {
                    val x = line.extractNumbers().toInt()
                    cur = cur.substring(cur.length - x) + cur.substring(0, cur.length - x)
                }
                line.startsWith("rotate based") -> {
                    val x = line.split().last()
                    val index = cur.indexOf(x)
                    cur = cur.rotateRight(1 + index + if (index >= 4) 1 else 0)
                }
                line.startsWith("reverse positions") -> {
                    val (x, y) = line.extractNumbersSeparated()
                    cur = cur.substring(0, x) + cur.substring(x, y + 1).reversed() + cur.substring(y + 1)
                }
                line.startsWith("move position") -> {
                    val (x, y) = line.extractNumbersSeparated()
                    val c = cur[x]
                    cur = cur.substring(0, x) + cur.substring(x + 1)
                    cur = cur.substring(0, y) + c + cur.substring(y)
                }
            }
        }
        return cur
    }
}