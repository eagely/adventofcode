package solutions.y2016

import Solution
import utils.*
import utils.annotations.NoTest
import java.io.File

class Day4 : Solution(2016) {

    override fun solvePart1(input: File) = input.lines.map { it.remove('-').dropLast(1).split('[').toPair() }.sumOf { (n, c) -> if (n.extractLetters().counts().toList().sortedBy { it.first }.sortedByDescending { it.second }.take(5).map { it.first }.join() == c) n.extractNumbers().toInt() else 0 }

    @NoTest
    override fun solvePart2(input: File) = input.lines.first { it.before('[').extractLetters().rotN(it.extractNumbers().toInt()) == "northpoleobjectstorage" }.extractNumbers()
}