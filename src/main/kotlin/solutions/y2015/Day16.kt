package solutions.y2015

import Solution
import java.io.File
import utils.*

class Day16 : Solution(2015) {

    override fun solvePart1(input: File): Any {
        val required = mapOf("children" to 3,
            "cats" to 7,
            "samoyeds" to 2,
            "pomeranians" to 3,
            "akitas" to 0,
            "vizslas" to 0,
            "goldfish" to 5,
            "trees" to 3,
            "cars" to 2,
            "perfumes" to 1)

        return input.lines.map { it.extractNumbersSeparated()[0] to it.after(": ").split(", ").associate { it.before(":") to it.extractNumbers().toInt() } }.first { it.second.all { it.key !in required.keys || required[it.key] == it.value } }.first
    }

    override fun solvePart2(input: File): Any {
        val max = input.text.extractNumbersSeparated().max()
        val required = mapOf(
            "children" to 3..3,
            "cats" to 8..max,
            "samoyeds" to 2..2,
            "pomeranians" to 0..2,
            "akitas" to 0..0,
            "vizslas" to 0..0,
            "goldfish" to 0..4,
            "trees" to 4..max,
            "cars" to 2..2,
            "perfumes" to 1..1
        )

        return input.lines.map { it.extractNumbersSeparated()[0] to it.after(": ").split(", ").associate { it.before(":") to it.extractNumbers().toInt() } }.first {
            it.second.all { it.key !in required.keys || it.value in required[it.key]!! }
        }.first
    }
}