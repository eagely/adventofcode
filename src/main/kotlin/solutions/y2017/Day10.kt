package solutions.y2017

import Solution
import java.io.File
import utils.*

class Day10 : Solution(2017) {

    override fun solvePart1(input: File) = hash((0..255).toList(), input.text.split(",").map { it.toInt() }).let { it.third[0] * it.third[1] }

    override fun solvePart2(input: File): Any {
        val lengths = input.text.map { it.code } + listOf(17, 31, 73, 47, 23)
        var nums = (0..255).toList()
        var pos = 0
        var skip = 0
        repeat(64) {
            val res = hash(nums, lengths, pos, skip)
            pos = res.first
            skip = res.second
            nums = res.third
        }
        return nums.chunked(16).map { it.reduce { acc, i -> acc xor i } }.joinToString("") { it.toString(16).padStart(2, '0') }
    }

    private fun hash(list: List<Int>, lengths: List<Int>, ipos: Int = 0, iskip: Int = 0): Triple<Int, Int, List<Int>> {
        var nums = list
        var pos = ipos
        var skip = iskip

        for (l in lengths) {
            if (pos + l > nums.size) {
                val rev = (nums.subList(pos, nums.size) + nums.subList(0, pos + l - nums.size)).reversed()
                nums = (rev.takeLast(rev.size - (nums.size - pos)) + nums.subList(pos + l - nums.size, pos) + rev.take(nums.size - pos)).toMutableList()
            }
            else {
                val rev = nums.subList(pos, pos + l).reversed()
                nums = (nums.subList(0, pos) + rev + nums.subList(pos + l, nums.size)).toMutableList()
            }
            pos = (pos + l + skip++) % nums.size
        }
        return Triple(pos, skip, nums)
    }
}