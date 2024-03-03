package solutions.y2017

import Solution
import utils.grid.Grid
import utils.hexToBin
import utils.join
import utils.text
import java.io.File

class Day14 : Solution(2017) {

    override fun solvePart1(input: File) = input.text.let { text -> (0..127).map { hash("$text-$it") }.join().hexToBin().count { it == '1' } }

    override fun solvePart2(input: File) = input.text.let { text -> Grid.of((0..127).map { hash("$text-$it").hexToBin() }) }.groups('0').size

    private fun hash(input: String): String {
        val lengths = input.map { it.code } + listOf(17, 31, 73, 47, 23)
        var nums = (0..255).toList()
        var pos = 0
        var skip = 0
        repeat(64) {
            for (l in lengths) {
                if (pos + l > nums.size) {
                    val rev = (nums.subList(pos, nums.size) + nums.subList(0, pos + l - nums.size)).reversed()
                    nums = (rev.takeLast(rev.size - (nums.size - pos)) + nums.subList(pos + l - nums.size, pos) + rev.take(nums.size - pos)).toMutableList()
                } else {
                    val rev = nums.subList(pos, pos + l).reversed()
                    nums = (nums.subList(0, pos) + rev + nums.subList(pos + l, nums.size)).toMutableList()
                }
                pos = (pos + l + skip++) % nums.size
            }
        }
        return nums.chunked(16).map { it.reduce { acc, i -> acc xor i } }.joinToString("") { it.toString(16).padStart(2, '0') }
    }
}