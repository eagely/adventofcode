package solutions.y2017

import Solution
import java.io.File
import utils.*

class Day5 : Solution(2017) {

    override fun solvePart1(input: File): Any {
        val list = input.ril().toMutableList()
        var i = 0
        var c = 0
        while (i >= 0 && i < list.size) {
            c++
            list[i]++
            i += list[i] - 1
        }
        return c
    }

    override fun solvePart2(input: File): Any {
        val list = input.ril().toMutableList()
        var i = 0
        var c = 0
        while (i >= 0 && i < list.size) {
            c++
            val temp = list[i]
            if (list[i] >= 3) list[i]--
            else list[i]++
            i += temp
        }
        return c
    }
}