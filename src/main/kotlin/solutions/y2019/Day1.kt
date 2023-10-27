package solutions.y2019

import Solution
import java.io.File

class Day1 : Solution(2019) {
    override fun solvePart1(input: File): String {
        return input.readLines().sumOf { it.toInt() / 3 - 2 }.toString()
    }

    override fun solvePart2(input: File): String {
        var lines = input.readLines().map { it.toInt() }
        var output = 0L
        for(i in lines) {
            var fuel = i / 3 - 2
            while(fuel > 0) {
                output += fuel
                fuel = fuel / 3 - 2
            }
        }
        return output.toString()
    }
}