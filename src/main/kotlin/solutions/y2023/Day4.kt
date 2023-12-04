package solutions.y2023

import Solution
import utils.Utils.pow
import utils.Utils.rl
import java.io.File

class Day4 : Solution(2023) {
    override fun solvePart1(input: File) = input.rl().map { it.substringAfter(": ").split(" | ").map { it.split(" ").filterNot { it.isBlank() } } }.sumOf { 2.pow(it.first().intersect(it.last().toSet()).size) / 2 }
    override fun solvePart2(input: File) = MutableList(input.rl().size) { 1 }.apply { input.rl().map { it.substringAfter(": ").split(" | ").map { it.split(" ").filterNot { it.isBlank() } } }.forEachIndexed { i, l ->  (1..l.first().intersect(l.last().toSet()).size).filter { i + it < size }.forEach { this[i + it] += this[i] } } }.sum()
}
