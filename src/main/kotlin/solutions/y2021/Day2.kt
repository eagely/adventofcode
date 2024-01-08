package solutions.y2021

import Solution
import utils.Utils.rl
import java.io.File

class Day2 : Solution(2021) {

    override fun solvePart1(input: File): Any {
        var h = 0
        var d = 0

        input.rl().forEach {
            val (arg, value) = it.split(" ")
            when (arg) {
                "forward" -> h += value.toInt()
                "up" -> d -= value.toInt()
                "down" -> d += value.toInt()
            }
        }
        return h * d
    }

    override fun solvePart2(input: File): Any {
        var h = 0
        var d = 0
        var aim = 0

        input.rl().forEach {
            val (arg, value) = it.split(" ")
            when (arg) {
                "forward" -> {
                    d += aim * value.toInt()
                    h += value.toInt()
                }
                "up" -> aim -= value.toInt()
                "down" -> aim += value.toInt()
            }
        }
        return h * d
    }
}