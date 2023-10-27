package solutions

import Solution
import java.io.File

class Day4 : Solution(2019) {
    override fun solvePart1(input: File): String {
        val parts = input.readText().trim().split("-").map { it.toInt() }
        val range = parts.first() .. parts.last()
        var output = 0
        var j: String
        outer@ for(i in range) {
            j = i.toString()
            if(j.length != 6) continue
            if(j.map { it.toString().toInt() }.zipWithNext().any { (a, b) -> a > b }) continue@outer
            if(j.map { it.toString().toInt() }.zipWithNext().any {(a,b) -> a == b}) output++
        }
        return output.toString()
    }

    override fun solvePart2(input: File): String {
        val parts = input.readText().trim().split("-").map { it.toInt() }
        val range = parts.first() .. parts.last()
        var output = 0
        var j: String
        outer@ for(i in range) {
            j = i.toString()
            if(j.length != 6) continue
            if(!checkIfGood(j.map { it.toString().toInt() })) continue
            if(j.map { it.toString().toInt() }.zipWithNext().any { (a, b) -> a > b }) continue
            output++
        }
        return output.toString()
    }

    private fun checkIfGood(input: List<Int>): Boolean {
        for(i in input) {
            if(input.count { it == i } == 2 && input.zipWithNext().any {(a,b) -> a == i && b == i}) {
                return true
            }
        }
        return false
    }
}